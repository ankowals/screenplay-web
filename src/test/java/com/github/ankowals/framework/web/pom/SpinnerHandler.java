package com.github.ankowals.framework.web.pom;

import java.time.Duration;
import java.util.Arrays;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public record SpinnerHandler(
    WebDriver webDriver, WebDriverWait webDriverWait, By... spinnerLocators) {

  public SpinnerHandler(WebElement webElement, WebDriverWait webDriverWait, By... spinnerLocators) {
    this(((WrapsDriver) webElement).getWrappedDriver(), webDriverWait, spinnerLocators);
  }

  public void handle() {
    ExpectedCondition<?>[] expectedConditions =
        Arrays.stream(this.spinnerLocators)
            .map(ExpectedConditions::visibilityOfElementLocated)
            .toArray(ExpectedCondition[]::new);

    // waits up to 500 ms for spinner to appear
    try {
      new WebDriverWait(this.webDriver, Duration.ofMillis(500), Duration.ofMillis(100))
          .until(ExpectedConditions.or(expectedConditions));
    } catch (TimeoutException ignored) { // NOSONAR
    }

    // waits longer for spinner to disappear
    this.webDriverWait.until(ExpectedConditions.not(ExpectedConditions.or(expectedConditions)));
  }
}
