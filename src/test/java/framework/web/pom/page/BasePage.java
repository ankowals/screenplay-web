package framework.web.pom.page;

import org.openqa.selenium.*;

// can be used to represent particular page which can be open in the browser
// can be composed of multiple page elements and views
public abstract class BasePage extends BasePageElement {

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
