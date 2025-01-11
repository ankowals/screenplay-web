package tests;

import base.TestBase;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.See;
import framework.web.screenplay.BrowseTheWeb;
import io.github.bonigarcia.seljup.SingleSession;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;
import pom.saucedemo.CartPage;
import pom.saucedemo.CheckoutOverviewPage;
import screenplay.saucedemo.*;

/** test from SauceOrderSummaryTest re-written using screenplay */
@SingleSession
@DisableIfTestFails
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SauceOrderSummaryScreenplayTest extends TestBase {

  Actor user;

  @BeforeEach
  void beforeEach() {
    this.user = new Actor();
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
        Checkout.as(
            form ->
                form.enterFirstName("terefere")
                    .enterLastName("hopsiasia")
                    .enterPostalCode("123")
                    .clickContinue()));

    CheckoutOverviewPage.SummaryInfo summaryInfo = this.user.asksFor(TheOrder.summaryInfo());

    this.user
        .should(See.that(summaryInfo))
        .returns("SauceCard #31337", CheckoutOverviewPage.SummaryInfo::paymentInfo)
        .returns("Free Pony Express Delivery!", CheckoutOverviewPage.SummaryInfo::shippingInfo);

    this.user
        .should(See.that(summaryInfo.priceTotal()))
        .returns("$39.98", CheckoutOverviewPage.PriceTotal::itemTotal)
        .returns("$3.20", CheckoutOverviewPage.PriceTotal::tax)
        .returns("$43.18", CheckoutOverviewPage.PriceTotal::total);
  }
}
