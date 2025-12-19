package screenplay.saucedemo.interactions;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import framework.web.screenplay.ManageBrowsers;
import pom.saucedemo.LoginPage;

public class Login {
  public static Interaction with(Credentials credentials) {
    return actor -> {
      BrowseTheWeb.as(actor).onPage(LoginPage.class).open();

      if (Boolean.parseBoolean(System.getenv("BROWSER_WATCHER_ENABLED"))) {
        actor
            .usingAbilityTo(ManageBrowsers.class)
            .webDriverManager()
            .startRecording(actor.usingAbilityTo(BrowseTheWeb.class).driver());
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
