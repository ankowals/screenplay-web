package com.github.ankowals.tests;

import com.github.ankowals.base.TestBase;
import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.ManageBrowsers;
import com.github.ankowals.domain.saucedemo.interactions.Add;
import com.github.ankowals.domain.saucedemo.interactions.Checkout;
import com.github.ankowals.domain.saucedemo.interactions.Login;
import com.github.ankowals.domain.saucedemo.pom.CartPage;
import com.github.ankowals.domain.saucedemo.pom.CheckoutOverviewPage;
import com.github.ankowals.domain.saucedemo.questions.TheItem;
import com.github.ankowals.domain.saucedemo.questions.TheOrder;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.actor.Actors;
import com.github.ankowals.framework.screenplay.helpers.See;
import io.github.bonigarcia.seljup.SingleSession;
import org.assertj.core.api.Assertions;
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
    this.user.can(ManageBrowsers.with(SELENIUM_JUPITER.getConfig().getManager()));
  }

  @Test
  @Order(1)
  void shouldValidateItemInCart() throws Exception {
    this.user.attemptsTo(
        Login.with(new Login.Credentials("standard_user", "secret_sauce")),
        Add.toCart("Sauce Labs Bike Light"),
        Add.toCart("Sauce Labs Backpack"));

    this.user.should(
        See.eventually(
            TheItem.inCart("Sauce Labs Bike Light"),
            item -> {
              Assertions.assertThat(item)
                  .returns("Sauce Labs Bike Light", CartPage.CartItem::name)
                  .returns(1, CartPage.CartItem::quantity)
                  .returns("$9.99", CartPage.CartItem::price);

              Assertions.assertThat(item.description()).contains("1 AAA battery included");
            }));
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
            summaryInfo -> {
              Assertions.assertThat(summaryInfo)
                  .returns("SauceCard #31337", CheckoutOverviewPage.SummaryInfo::paymentInfo)
                  .returns(
                      "Free Pony Express Delivery!",
                      CheckoutOverviewPage.SummaryInfo::shippingInfo);

              Assertions.assertThat(summaryInfo.priceTotal())
                  .returns("$39.98", CheckoutOverviewPage.PriceTotal::itemTotal)
                  .returns("$3.20", CheckoutOverviewPage.PriceTotal::tax)
                  .returns("$43.18", CheckoutOverviewPage.PriceTotal::total);
            }));
  }
}
