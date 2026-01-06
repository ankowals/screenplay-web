package tests;

import base.TestBase;
import domain.BrowseTheWeb;
import domain.ManageBrowsers;
import domain.saucedemo.interactions.Login;
import domain.saucedemo.questions.TheErrorMessage;
import framework.screenplay.actor.Actor;
import framework.screenplay.actor.Actors;
import framework.screenplay.helpers.See;
import java.util.concurrent.TimeUnit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BrowserWatcherRecordingTest extends TestBase {

  private Actor user;

  @BeforeEach
  void beforeEach() {
    this.user = Actors.withAbilities();
    this.user.can(BrowseTheWeb.with(this.browser));
    this.user.can(
        ManageBrowsers.with(
            SELENIUM_JUPITER
                .getConfig()
                .getManager())); // to support video recording via Login interaction
  }

  // recording has to be started after opening url in the browser
  // active tab is recorded
  @Test
  void shouldSeeMessageWhenPasswordIsWrong() throws Exception {
    this.user.attemptsTo(Login.with(new Login.Credentials("standard_user", "terefere")));

    TimeUnit.MILLISECONDS.sleep(1500); // NOSONAR, extension needs a bit time to warm up

    this.user.should(
        See.that(
            TheErrorMessage.uponLogin(),
            Matchers.containsString(
                "Username and password do not match any user in this service")));
  }
}
