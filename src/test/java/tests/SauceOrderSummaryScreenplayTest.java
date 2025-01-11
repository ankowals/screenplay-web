package tests;

import static org.hamcrest.Matchers.containsString;

import base.TestBase;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.See;
import framework.web.screenplay.BrowseTheWeb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import screenplay.saucedemo.*;

class SauceDemoScreenplayTest extends TestBase {

  Actor user;

  @BeforeEach
  void beforeEach() {
    this.user = new Actor();
    this.user.can(BrowseTheWeb.with(this.browser));
  }

  @Test
  void shouldSeeMessageUponWrongPasswordUsage() throws Exception {
    this.user.attemptsTo(Login.with(new Login.Credentials("standard_user", "terefere")));

    this.user.should(
        See.that(
            ErrorMessage.uponLogin(),
            containsString("Username and password do not match any user in this service")));
  }

  @Test
  void shouldLoginSuccessfully() throws Exception {
    this.user.attemptsTo(Login.with(new Login.Credentials("standard_user", "secret_sauce")));

    this.user.should(See.that(ThePage.title())).isEqualTo("Products");
  }

  @Test
  void shouldSeeOrderSummary() throws Exception {
    this.user.attemptsTo(
        Login.with(new Login.Credentials("standard_user", "secret_sauce")),
        Add.toCart("Sauce Labs Bike Light"),
        Add.toCart("Sauce Labs Backpack"),
        Checkout.as(
            form ->
                form.enterFirstName("terefere")
                    .enterLastName("hopsiasia")
                    .enterPostalCode("123")
                    .clickContinue()));

    this.user.should(See.that(ThePage.title())).isEqualTo("Checkout: Overview");
  }
}
