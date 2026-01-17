package com.github.ankowals.framework.web.pom.page;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

// can be used to represent particular page which can be open in the browser
// can be composed of multiple pages and views
public abstract class BasePage extends AbstractPage {

  public BasePage(WebDriver driver) {
    super(driver);
  }

  public String getTitle() {
    return this.driver.getTitle();
  }

  public void open(String url) {
    this.driver.get(url);
  }

  public byte[] takeScreenshot() {
    return ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.BYTES);
  }

  public byte[] takeScreenshot(WebElement webElement) {
    return webElement.getScreenshotAs(OutputType.BYTES);
  }

  protected WebDriver.Navigation navigate() {
    return this.driver.navigate();
  }

  protected Actions createAction() {
    return new Actions(this.driver);
  }
}
