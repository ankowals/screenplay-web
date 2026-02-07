package com.github.ankowals.framework.web.pom.elements.common;

import com.github.ankowals.framework.web.pom.elements.Button;
import java.time.Duration;
import java.util.Arrays;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// awaits for a spinner to be removed or not displayed in the DOM
public class SpinnerTriggeringButton implements Button {

  private final WebElement webElement;
  private final WebDriverWait webDriverWait;
  private final ExpectedCondition<?>[] expectedConditions;

  private SpinnerTriggeringButton(
      WebElement webElement, WebDriverWait webDriverWait, By... spinnersLocators) {
    this.webElement = webElement;
    this.webDriverWait = webDriverWait;
    this.expectedConditions =
        Arrays.stream(spinnersLocators)
            .map(ExpectedConditions::visibilityOfElementLocated)
            .toArray(ExpectedCondition[]::new);
  }

  public static SpinnerTriggeringButton of(
      WebElement webElement, WebDriverWait webDriverWait, By... spinnersLocators) {
    return new SpinnerTriggeringButton(webElement, webDriverWait, spinnersLocators);
  }

  @Override
  public void click() {
    this.webElement.click();

    // waits up to 500 ms for spinner to appear
    try {
      this.wait(this.webElement, Duration.ofMillis(500), Duration.ofMillis(100))
          .until(ExpectedConditions.or(this.expectedConditions));
    } catch (TimeoutException ignored) { // NOSONAR
    }

    // waits long for spinner to disappear
    this.webDriverWait.until(
        ExpectedConditions.not(ExpectedConditions.or(this.expectedConditions)));
  }

  @Override
  public String getText() {
    return this.webElement.getAttribute("title");
  }

  private WebDriverWait wait(WebElement webElement, Duration timeout, Duration sleep) {
    WebDriver webDriver = ((WrapsDriver) webElement).getWrappedDriver();
    return new WebDriverWait(webDriver, timeout, sleep);
  }
}
