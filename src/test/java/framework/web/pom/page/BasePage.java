package framework.web.pom.page;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

public abstract class BasePage {

  protected final WebDriver driver;
  protected final JavascriptExecutor js;

  public BasePage(WebDriver driver) {
    this.driver = driver;
    this.js = (JavascriptExecutor) this.driver;
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

  protected Actions createAction() {
    return new Actions(this.driver);
  }

  protected WebDriver.Navigation navigate() {
    return this.driver.navigate();
  }

  protected WebDriver.TargetLocator switchTo() {
    return this.driver.switchTo();
  }

  protected boolean isInsideFrame() {
    return this.js.executeScript("return window.frameElement") != null;
  }
}
