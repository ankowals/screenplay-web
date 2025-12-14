package tests;

import base.TestBase;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.See;
import framework.web.screenplay.BrowseTheWeb;
import java.util.concurrent.TimeUnit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import screenplay.saucedemo.interactions.Login;
import screenplay.saucedemo.questions.TheErrorMessage;

class BrowserWatcherRecordingTest extends TestBase {

  Actor user;

  @BeforeEach
  void beforeEach() {
    this.user = new Actor();
    this.user.can(BrowseTheWeb.with(this.browser));
  }

  // recording has to be started after opening url in the browser
  // active tab is recorded
  @Test
  void shouldSeeMessageWhenPasswordIsWrong() throws Exception {
    this.user.attemptsTo(
        Login.with(
            new Login.Credentials("standard_user", "terefere"),
            this.startRecording(this.user.usingAbilityTo(BrowseTheWeb.class).driver())));

    TimeUnit.MILLISECONDS.sleep(1500); // NOSONAR, extension needs a bit time to warm up

    this.user.should(
        See.that(
            TheErrorMessage.uponLogin(),
            Matchers.containsString(
                "Username and password do not match any user in this service")));
  }
}
