package screenplay.saucedemo.interactions;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import framework.web.wdm.RecordingEnabler;
import java.util.concurrent.TimeUnit;
import pom.saucedemo.LoginPage;

public class Login {
  public static Interaction with(Credentials credentials, RecordingEnabler recordingEnabler) {
    return actor -> {
      BrowseTheWeb.as(actor).onPage(LoginPage.class).open();

      recordingEnabler.start(); // starts recording with BrowserWatcher
      TimeUnit.MILLISECONDS.sleep(1500); // NOSONAR, extension needs a bit time to warm up

      BrowseTheWeb.as(actor)
          .onPage(LoginPage.class)
          .enterUsername(credentials.username())
          .enterPassword(credentials.password())
          .clickLogin();
    };
  }

  public static Interaction with(Credentials credentials) {
    return actor ->
        BrowseTheWeb.as(actor)
            .onPage(LoginPage.class)
            .open()
            .enterUsername(credentials.username())
            .enterPassword(credentials.password())
            .clickLogin();
  }

  public record Credentials(String username, String password) {}
}
