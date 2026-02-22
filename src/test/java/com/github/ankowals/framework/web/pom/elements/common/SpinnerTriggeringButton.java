package com.github.ankowals.framework.web.pom.elements.common;

import java.time.Duration;
import java.util.Arrays;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// awaits for a spinner to be removed or not displayed in the DOM
public class SpinnerTriggeringButton extends ButtonImpl {

  private final WebDriverWait webDriverWait;
  private final By[] spinnersLocators;

  private SpinnerTriggeringButton(
      WebElement webElement, WebDriverWait webDriverWait, By... spinnersLocators) {
    super(webElement);
    this.webDriverWait = webDriverWait;
    this.spinnersLocators = spinnersLocators;
  }

  public static SpinnerTriggeringButton of(
      WebElement webElement, WebDriverWait webDriverWait, By... spinnersLocators) {
    return new SpinnerTriggeringButton(webElement, webDriverWait, spinnersLocators);
  }

  @Override
  public void click() {
    this.webElement.click();

    ExpectedCondition<?>[] expectedConditions =
        Arrays.stream(this.spinnersLocators)
            .map(ExpectedConditions::visibilityOfElementLocated)
            .toArray(ExpectedCondition[]::new);

    // waits up to 500 ms for spinner to appear
    try {
      this.wait(this.webElement, Duration.ofMillis(500), Duration.ofMillis(100))
          .until(ExpectedConditions.or(expectedConditions));
    } catch (TimeoutException ignored) { // NOSONAR
    }

    WebDriver webDriver = ((WrapsDriver) this.webElement).getWrappedDriver();

    expectedConditions =
        Arrays.stream(this.spinnersLocators)
            .map(by -> ExpectedConditions.invisibilityOfAllElements(webDriver.findElements(by)))
            .toArray(ExpectedCondition[]::new);

    // waits longer for spinner to disappear
    this.webDriverWait.until(ExpectedConditions.and(expectedConditions));
  }

  private WebDriverWait wait(WebElement webElement, Duration timeout, Duration sleep) {
    WebDriver webDriver = ((WrapsDriver) webElement).getWrappedDriver();
    return new WebDriverWait(webDriver, timeout, sleep);
  }
}
