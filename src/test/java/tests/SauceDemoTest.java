package tests;

import base.SingleSessionTestBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;
import pom.saucedemo.LoginPage;
import pom.saucedemo.ProductsPage;

@DisableIfTestFails
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SauceDemoTest extends SingleSessionTestBase {

  LoginPage loginPage;

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
    ProductsPage productsPage =
        this.loginPage.enterUsername("standard_user").enterPassword("secret_sauce").clickLogin();

    String actual = productsPage.getTitle();

    Assertions.assertThat(actual).contains("Products");
  }
}
