package tests;

import base.SingleSessionTestBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;
import pom.saucedemo.*;

import java.util.List;

@DisableIfTestFails
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SauceDemoTest extends SingleSessionTestBase {

  LoginPage loginPage;
  ProductsPage productsPage;
  CartPage cartPage;

  @BeforeEach
  void beforeEach() {
    this.loginPage = new LoginPage(this.browser);
  }

  @Test
  @Order(1)
  void shouldSeeMessageUponWrongPasswordUsage() {
    this.loginPage.open().enterUsername("standard_user").enterPassword("terefere").clickLogin();

    String actual = this.loginPage.getErrorText();

    Assertions.assertThat(actual)
        .contains("Username and password do not match any user in this service");
  }

  @Test
  @Order(2)
  void shouldLoginSuccessfully() {
    this.productsPage =
        this.loginPage.enterUsername("standard_user").enterPassword("secret_sauce").clickLogin();

    String actual = this.productsPage.getTitle();

    Assertions.assertThat(actual).contains("Products");
  }

  @Test
  @Order(3)
  void shouldAddToCart() {
    this.cartPage = this.productsPage.clickAddToCart("Sauce Labs Bike Light")
            .clickAddToCart("Sauce Labs Backpack")
            .clickCartButton();

    List<CartPage.CartItem> actual = this.cartPage.getCartItems();
    Assertions.assertThat(actual).isNotEmpty();
  }

  @Test
  @Order(4)
  void shouldValidateItemInCart() {
    CartPage.CartItem bikeLightItem = this.cartPage.getCartItems()
            .stream()
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
  @Order(5)
  void shouldSeeOrderSummary() {
    CheckoutOverviewPage checkoutOverviewPage = this.cartPage.clickCheckout()
            .enterFirstName("terefere")
            .enterLastName("hopsiasia")
            .enterPostalCode("123")
            .clickContinue();

    String actual = checkoutOverviewPage.getTitle();

    Assertions.assertThat(actual).isEqualTo("Checkout: Overview");
  }
}
