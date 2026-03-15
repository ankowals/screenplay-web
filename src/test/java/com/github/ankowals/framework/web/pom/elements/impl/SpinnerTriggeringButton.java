package com.github.ankowals.framework.web.pom.elements.impl;

import com.github.ankowals.framework.web.pom.SpinnerHandler;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;

// awaits for a spinner to be removed or not displayed in the DOM
public class SpinnerTriggeringButton extends ButtonImpl {

  private final SpinnerHandler spinnerHandler;

  protected SpinnerTriggeringButton(
      WebElement webElement, WebDriverWait webDriverWait, By... spinnersLocators) {
    super(webElement);
    this.spinnerHandler = new SpinnerHandler(webElement, webDriverWait, spinnersLocators);
  }

  @Override
  public void click() {
    this.webElement.click();
    this.spinnerHandler.handle();
  }
}
