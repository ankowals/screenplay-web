package com.github.ankowals.tests;

import com.github.ankowals.base.TestBase;
import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.ManageBrowsers;
import com.github.ankowals.domain.saucedemo.interactions.Login;
import com.github.ankowals.domain.saucedemo.questions.TheErrorMessage;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.actor.Actors;
import com.github.ankowals.framework.screenplay.helpers.See;
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
        See.eventually(
            TheErrorMessage.uponLogin(),
            Matchers.containsString(
                "Username and password do not match any user in this service")));
  }
}
