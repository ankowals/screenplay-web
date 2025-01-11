package tests;

import base.TestBase;
import io.github.bonigarcia.seljup.SingleSession;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;
import pom.saucedemo.*;

/**
 * In parallel execution each method requires separate driver instance and with @SingleSession
 * enabled we have a single instance shared for all tests. To run test configure parallel execution
 * so that top-level classes run in parallel but methods in same thread.
 *
 * <p>junit.jupiter.execution.parallel.enabled=true
 * junit.jupiter.execution.parallel.mode.default=same_thread
 * junit.jupiter.execution.parallel.mode.classes.default=concurrent
 *
 * <p>Uses (<a href="http://xunitpatterns.com/Chained%20Tests.html">test chaining</a>)
 */
@SingleSession
@DisableIfTestFails
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SauceOrderSummaryTest extends TestBase {

  ProductsPage productsPage;
  CartPage cartPage;
  CheckoutOverviewPage checkoutOverviewPage;

  @Test
  @Order(1)
  void shouldLoginSuccessfully() {
    this.productsPage =
        new LoginPage(this.browser)
            .open()
            .enterUsername("standard_user")
            .enterPassword("secret_sauce")
            .clickLogin();

    String actual = this.productsPage.getTitle();

    Assertions.assertThat(actual).contains("Products");
  }

  @Test
  @Order(2)
  void shouldAddToCart() {
    this.cartPage =
        this.productsPage
            .clickAddToCart("Sauce Labs Bike Light")
            .clickAddToCart("Sauce Labs Backpack")
            .clickCartButton();

    List<CartPage.CartItem> actual = this.cartPage.getCartItems();
    Assertions.assertThat(actual).isNotEmpty();
  }

  @Test
  @Order(3)
  void shouldValidateItemInCart() {
    CartPage.CartItem bikeLightItem =
        this.cartPage.getCartItems().stream()
            .filter(item -> item.name().contains("Bike Light"))
            .findFirst()
            .orElseThrow();

    Assertions.assertThat(bikeLightItem)
        .returns("Sauce Labs Bike Light", CartPage.CartItem::name)
        .returns(1, CartPage.CartItem::quantity)
        .returns("$9.99", CartPage.CartItem::price);

    Assertions.assertThat(bikeLightItem.description()).contains("1 AAA battery included");
  }

  @Test
  @Order(4)
  void shouldCheckOrderSummary() {
    this.checkoutOverviewPage =
        this.cartPage
            .clickCheckout()
            .enterFirstName("terefere")
            .enterLastName("hopsiasia")
            .enterPostalCode("123")
            .clickContinue();

    CheckoutOverviewPage.SummaryInfo actual = this.checkoutOverviewPage.getSummaryInfo();

    Assertions.assertThat(actual)
        .returns("SauceCard #31337", CheckoutOverviewPage.SummaryInfo::paymentInfo)
        .returns("Free Pony Express Delivery!", CheckoutOverviewPage.SummaryInfo::shippingInfo);

    Assertions.assertThat(actual.priceTotal())
        .returns("$39.98", CheckoutOverviewPage.PriceTotal::itemTotal)
        .returns("$3.20", CheckoutOverviewPage.PriceTotal::tax)
        .returns("$43.18", CheckoutOverviewPage.PriceTotal::total);
  }
}
