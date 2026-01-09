package framework.web.pom.page;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

abstract class AbstractPage {

  protected final WebDriver driver;
  protected final JavascriptExecutor js;

  AbstractPage(WebDriver driver) {
    this.driver = driver;
    this.js = (JavascriptExecutor) this.driver;
  }

  protected WebDriver.TargetLocator switchTo() {
    return this.driver.switchTo();
  }

  protected boolean isInsideFrame() {
    return this.js.executeScript("return window.frameElement") != null;
  }
}
