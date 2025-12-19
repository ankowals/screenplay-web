package tests;

import base.TestBase;
import framework.screenplay.helpers.See;
import java.util.concurrent.TimeUnit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import screenplay.saucedemo.interactions.Login;
import screenplay.saucedemo.questions.TheErrorMessage;

class BrowserWatcherRecordingTest extends TestBase {

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
