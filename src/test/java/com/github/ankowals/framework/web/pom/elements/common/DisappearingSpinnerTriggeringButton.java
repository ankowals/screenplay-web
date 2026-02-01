package com.github.ankowals.framework.web.pom.elements.common;

import com.github.ankowals.framework.web.pom.elements.Button;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

// awaits for a spinner to stop being displayed but it is not removed from DOM
public class DisappearingSpinnerTriggeringButton implements Button {

  private final WebElement webElement;
  private final WebDriverWait webDriverWait;
  private final By[] spinnersLocators;

  private DisappearingSpinnerTriggeringButton(
      WebElement webElement, WebDriverWait webDriverWait, By... spinnersLocators) {
    this.webElement = webElement;
    this.webDriverWait = webDriverWait;
    this.spinnersLocators = spinnersLocators;
  }

  public static DisappearingSpinnerTriggeringButton of(
      WebElement webElement, WebDriverWait webDriverWait, By... spinnersLocators) {
    return new DisappearingSpinnerTriggeringButton(webElement, webDriverWait, spinnersLocators);
  }

  @Override
  public void click() {
    this.webElement.click();

    // waits up to 500 ms for spinner to appear
    ExpectedCondition<?>[] expectedConditions =
        Arrays.stream(this.spinnersLocators)
            .map(ExpectedConditions::visibilityOfElementLocated)
            .toArray(ExpectedCondition[]::new);

    try {
      this.wait(this.webElement, Duration.ofMillis(500), Duration.ofMillis(100))
          .until(ExpectedConditions.or(expectedConditions));
    } catch (TimeoutException ignored) { // NOSONAR
    }

    this.webDriverWait.until(this.invisibilityOfSpinner(this.spinnersLocators));
  }

  @Override
  public String getText() {
    return this.webElement.getAttribute("title");
  }

  private WebDriverWait wait(WebElement webElement, Duration timeout, Duration sleep) {
    WebDriver webDriver = ((WrapsDriver) webElement).getWrappedDriver();
    return new WebDriverWait(webDriver, timeout, sleep);
  }

  private ExpectedCondition<Boolean> invisibilityOfSpinner(By... spinnersLocators) {
    return webDriver -> {
      Objects.requireNonNull(webDriver);
      List<WebElement> elements = new ArrayList<>();

      Arrays.stream(spinnersLocators).forEach(by -> elements.addAll(webDriver.findElements(by)));

      return ExpectedConditions.invisibilityOfAllElements(elements).apply(webDriver);
    };
  }
}
