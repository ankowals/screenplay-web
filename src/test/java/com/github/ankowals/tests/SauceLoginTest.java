package com.github.ankowals.tests;

import com.github.ankowals.base.TestBase;
import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.saucedemo.interactions.Login;
import com.github.ankowals.domain.saucedemo.pom.LoginPage;
import com.github.ankowals.domain.saucedemo.questions.TheErrorMessage;
import com.github.ankowals.domain.saucedemo.questions.TheScreenshot;
import com.github.ankowals.framework.reporting.ExtentWebReportExtension;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.actor.Actors;
import com.github.ankowals.framework.screenplay.helpers.See;
import com.github.ankowals.framework.web.assertions.accessibility.AccessibilityAssert;
import com.github.ankowals.framework.web.assertions.visual.VisualAssert;
import io.github.bonigarcia.seljup.SingleSession;
import java.io.File;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junitpioneer.jupiter.DisableIfTestFails;
import org.openqa.selenium.By;

@SingleSession
@DisableIfTestFails
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SauceLoginTest extends TestBase {

  private Actor user;

  @BeforeEach
  void beforeEach() {
    this.user = Actors.withAbilities();
    this.user.can(BrowseTheWeb.with(this.browser));
  }

  @Test
  @Order(1)
  void shouldSeeMessageWhenPasswordIsWrong() throws Exception {
    this.user.attemptsTo(Login.with(new Login.Credentials("standard_user", "terefere")));

    this.user.should(
        See.eventually(
            TheErrorMessage.uponLogin(),
            Matchers.containsString(
                "Username and password do not match any user in this service")));
  }

  /*
  Needs to be run in container only, otherwise expect size mismatch exception
   */
  @Test
  @Order(2)
  @EnabledIfEnvironmentVariable(named = "BROWSER_IN_DOCKER_ENABLED", matches = "true")
  void shouldDisplayLoginForm(TestInfo testInfo) throws Exception {
    this.user.should(
        See.eventually(
            TheScreenshot.takenFor(testInfo),
            screenshot ->
                VisualAssert.assertThat(screenshot)
                    .excluding(this.browser.findElement(By.id("user-name")))
                    .isEqualTo("screenshots/login_page_viewport.png")));
  }

  @Test
  @Order(3)
  void shouldDisplayAccessibleLoginForm(TestInfo testInfo) {
    new LoginPage(this.browser).open();
    AccessibilityAssert.assertThatPage(this.browser)
        .reportAs(this.destination(testInfo), this.title(testInfo))
        .isViolationFree();
  }

  private File destination(TestInfo testInfo) {
    return new File(
        "%s/%s/%s-axe-%s.html"
            .formatted(
                ExtentWebReportExtension.REPORT_FILE.getParentFile(),
                testInfo.getTestClass().orElseThrow().getName(),
                testInfo.getTestMethod().orElseThrow().getName(),
                UUID.randomUUID()));
  }

  private String title(TestInfo testInfo) {
    return "%s.%s"
        .formatted(
            testInfo.getTestClass().orElseThrow().getSimpleName(),
            testInfo.getTestMethod().orElseThrow().getName());
  }
}
