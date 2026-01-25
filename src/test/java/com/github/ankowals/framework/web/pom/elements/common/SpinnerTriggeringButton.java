package com.github.ankowals.framework.web.pom.elements.common;

import com.github.ankowals.framework.web.pom.elements.Button;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SpinnerTriggeringButton implements Button {

  private final WebElement webElement;
  private final WebDriverWait webDriverWait;
  private final By spinnerLocator;

  private SpinnerTriggeringButton(
      WebElement webElement, WebDriverWait webDriverWait, By spinnerLocator) {
    this.webElement = webElement;
    this.webDriverWait = webDriverWait;
    this.spinnerLocator = spinnerLocator;
  }

  public static SpinnerTriggeringButton of(
      WebElement webElement, WebDriverWait webDriverWait, By spinnerLocator) {
    return new SpinnerTriggeringButton(webElement, webDriverWait, spinnerLocator);
  }

  @Override
  public void click() {
    this.webElement.click();

    // waits up to 500 ms for spinner to appear
    try {
      this.wait(this.webElement, Duration.ofMillis(500), Duration.ofMillis(100))
          .until(ExpectedConditions.visibilityOfElementLocated(this.spinnerLocator));
    } catch (TimeoutException ignored) { // NOSONAR
    }

    this.wait(this.invisibilityOfSpinner(this.spinnerLocator));
  }

  @Override
  public String getText() {
    return this.webElement.getAttribute("title");
  }

  private WebDriverWait wait(WebElement webElement, Duration timeout, Duration sleep) {
    WebDriver webDriver = ((WrapsDriver) webElement).getWrappedDriver();
    return new WebDriverWait(webDriver, timeout, sleep);
  }

  private void wait(ExpectedCondition<Boolean> condition) {
    this.webDriverWait.until(condition);
  }

  private ExpectedCondition<Boolean> invisibilityOfSpinner(By spinnerLocator) {
    return webDriver -> {
      Objects.requireNonNull(webDriver);
      List<WebElement> elements = webDriver.findElements(spinnerLocator);

      return ExpectedConditions.invisibilityOfAllElements(elements).apply(webDriver);
    };
  }
}
