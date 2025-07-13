package tests;

import base.TestBase;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.See;
import framework.web.accessibility.AccessibilityAssertions;
import framework.web.reporting.ExtentWebReportExtension;
import framework.web.screenplay.BrowseTheWeb;
import framework.web.visual.VisualAssertions;
import io.github.bonigarcia.seljup.SingleSession;
import java.io.IOException;
import java.text.ParseException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;
import org.openqa.selenium.By;
import pom.saucedemo.LoginPage;
import screenplay.saucedemo.interactions.Login;
import screenplay.saucedemo.questions.TheErrorMessage;

@SingleSession
@DisableIfTestFails
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
            Matchers.containsString(
                "Username and password do not match any user in this service")));
  }

  /*
  Needs to be run in container only, otherwise expect size mismatch exception
   */
  @Test
  @Order(2)
  void shouldDisplayLoginForm(TestInfo testInfo) throws IOException {
    VisualAssertions.assertThat(this.writeImage(this.takeScreenshot(), testInfo))
        .excluding(this.browser.findElement(By.id("user-name")))
        .isEqualTo("screenshots/login_page_viewport.png");
  }

  @Test
  @Order(3)
  void shouldDisplayAccessibleLoginForm(TestInfo testInfo) throws IOException, ParseException {
    new LoginPage(this.browser).open();
    AccessibilityAssertions.assertThatPage(this.browser)
        .reportAs(ExtentWebReportExtension.REPORT_FILE.getParentFile(), testInfo)
        .isViolationFree();
  }
}
