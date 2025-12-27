package tests;

import base.TestBase;
import domain.saucedemo.interactions.Add;
import domain.saucedemo.interactions.Checkout;
import domain.saucedemo.interactions.Login;
import domain.saucedemo.pom.CartPage;
import domain.saucedemo.pom.CheckoutOverviewPage;
import domain.saucedemo.questions.TheItem;
import domain.saucedemo.questions.TheOrder;
import framework.screenplay.actor.Actor;
import framework.screenplay.actor.Actors;
import framework.screenplay.helpers.See;
import framework.web.screenplay.BrowseTheWeb;
import io.github.bonigarcia.seljup.SingleSession;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;

/** test from SauceOrderSummaryTest re-written using screenplay */
@SingleSession
@DisableIfTestFails
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SauceOrderSummaryScreenplayTest extends TestBase {

  private Actor user;

  @BeforeEach
  void beforeEach() {
    this.user = Actors.withAbilities();
    this.user.can(BrowseTheWeb.with(this.browser));
  }

  @Test
  @Order(1)
  void shouldValidateItemInCart() throws Exception {
    this.user.attemptsTo(
        Login.with(new Login.Credentials("standard_user", "secret_sauce")),
        Add.toCart("Sauce Labs Bike Light"),
        Add.toCart("Sauce Labs Backpack"));

    CartPage.CartItem actualItem = this.user.asksFor(TheItem.inCart("Sauce Labs Bike Light"));

    this.user
        .should(See.that(actualItem))
        .returns("Sauce Labs Bike Light", CartPage.CartItem::name)
        .returns(1, CartPage.CartItem::quantity)
        .returns("$9.99", CartPage.CartItem::price);

    this.user
        .should(See.that(actualItem.description()))
        .asInstanceOf(InstanceOfAssertFactories.STRING)
        .contains("1 AAA battery included");
  }

  @Test
  @Order(2)
  void shouldCheckOrderSummary() throws Exception {
    this.user.attemptsTo(
        Checkout.completingForm(
            form ->
                form.enterFirstName("terefere")
                    .enterLastName("hopsiasia")
                    .enterPostalCode("123")
                    .clickContinue()));

    this.user.should(
        See.eventually(
            TheOrder.summaryInfo(),
            summaryInfo ->
                Assertions.assertThat(summaryInfo)
                    .returns("SauceCard #31337", CheckoutOverviewPage.SummaryInfo::paymentInfo)
                    .returns(
                        "Free Pony Express Delivery!",
                        CheckoutOverviewPage.SummaryInfo::shippingInfo)));

    this.user.should(
        See.eventually(
            TheOrder.summaryInfo(),
            summaryInfo ->
                Assertions.assertThat(summaryInfo.priceTotal())
                    .returns("$39.98", CheckoutOverviewPage.PriceTotal::itemTotal)
                    .returns("$3.20", CheckoutOverviewPage.PriceTotal::tax)
                    .returns("$43.18", CheckoutOverviewPage.PriceTotal::total)));
  }
}
