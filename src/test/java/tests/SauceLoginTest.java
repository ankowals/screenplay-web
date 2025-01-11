package tests;

import static org.hamcrest.Matchers.containsString;

import base.TestBase;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.See;
import framework.web.screenplay.BrowseTheWeb;
import org.junit.jupiter.api.*;
import screenplay.saucedemo.Login;
import screenplay.saucedemo.TheErrorMessage;

class SauceLoginTest extends TestBase {

  Actor user;

  @BeforeEach
  void beforeEach() {
    this.user = new Actor();
    this.user.can(BrowseTheWeb.with(this.browser));
  }

  @Test
  @Order(1)
  void shouldSeeMessageWhenPasswordIsWrong() throws Exception {
    this.user.attemptsTo(Login.with(new Login.Credentials("standard_user", "terefere")));

    this.user.should(
        See.that(
            TheErrorMessage.uponLogin(),
            containsString("Username and password do not match any user in this service")));
  }
}
