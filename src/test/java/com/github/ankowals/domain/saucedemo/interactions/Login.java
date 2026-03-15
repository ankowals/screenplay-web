package com.github.ankowals.domain.saucedemo.interactions;

import com.github.ankowals.abilities.BrowseTheWeb;
import com.github.ankowals.abilities.ManageBrowsers;
import com.github.ankowals.domain.saucedemo.pom.LoginPage;
import com.github.ankowals.framework.screenplay.Interaction;
import com.github.ankowals.framework.screenplay.helpers.use.UseAbility;
import com.github.ankowals.framework.web.devtools.DevToolsSupport;
import com.github.ankowals.framework.web.devtools.NetworkDomain;
import com.github.ankowals.framework.web.devtools.Session;
import com.github.ankowals.framework.web.storage.LocalStorage;
import com.github.ankowals.framework.web.storage.SessionStorage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.openqa.selenium.WebDriver;

public class Login {

  private static final Map<String, Session> SESSION_CACHE = new ConcurrentHashMap<>();

  public static Interaction andCacheWith(Credentials credentials) {
    return actor -> {
      boolean isBrowserWatcherEnabled =
          Boolean.parseBoolean(System.getenv("BROWSER_WATCHER_ENABLED"));
      Session session = SESSION_CACHE.get(credentials.username());

      // webdriver limitation, cookies can be set only after opening url in the browser
      // to overcome this limitation we will use devTools
      WebDriver webDriver = UseAbility.of(actor).to(BrowseTheWeb.class).driver();
      NetworkDomain networkDomain = new DevToolsSupport(webDriver).getNetworkDomain();

      if (session != null && !isBrowserWatcherEnabled) {
        // we may consider to call clear cookies first but for now assumed not needed when used with
        // new browser instance
        networkDomain.set(session.cookies());
      }

      BrowseTheWeb.as(actor).onPage(LoginPage.class).open();

      // webdrvier limitation, storage can be set only after opening url in the browser
      // would be better to use devTools to set it before calling open
      if (session != null && !isBrowserWatcherEnabled) {
        session.sessionStorage().set();
        session.localStorage().set();
      }

      if (isBrowserWatcherEnabled) {
        UseAbility.of(actor).to(ManageBrowsers.class).webDriverManager().startRecording(webDriver);
      }

      // devTools does not work with extensions installation via bidi
      if (session != null && !isBrowserWatcherEnabled) {
        return; // session restored, nothing else to do
      }

      BrowseTheWeb.as(actor)
          .onPage(LoginPage.class)
          .enterUsername(credentials.username())
          .enterPassword(credentials.password())
          .clickLogin();

      // we should verify that login was successful otherwise skip caching part

      // remember new session
      // it works only if login was successful otherwise exception can be thrown when getting
      // storage
      SESSION_CACHE.put(
          credentials.username(),
          new Session(
              networkDomain.getCookies(),
              new LocalStorage(webDriver),
              new SessionStorage(webDriver)));
    };
  }

  public static Interaction with(Credentials credentials) {
    return actor -> {
      BrowseTheWeb.as(actor).onPage(LoginPage.class).open();

      if (Boolean.parseBoolean(System.getenv("BROWSER_WATCHER_ENABLED"))) {
        UseAbility.of(actor)
            .to(ManageBrowsers.class)
            .webDriverManager()
            .startRecording(UseAbility.of(actor).to(BrowseTheWeb.class).driver());
      }

      BrowseTheWeb.as(actor)
          .onPage(LoginPage.class)
          .enterUsername(credentials.username())
          .enterPassword(credentials.password())
          .clickLogin();
    };
  }

  public record Credentials(String username, String password) {}
}
