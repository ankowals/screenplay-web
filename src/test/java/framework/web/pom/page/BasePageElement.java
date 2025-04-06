package framework.web.pom.page;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

// can be use to represent menu, panel etc. but can't be open as such via url
public abstract class BasePageElement {

  protected final WebDriver driver;
  protected final JavascriptExecutor js;

  public BasePageElement(WebDriver driver) {
    this.driver = driver;
    this.js = (JavascriptExecutor) this.driver;
  }

  public byte[] takeScreenshot(WebElement webElement) {
    return webElement.getScreenshotAs(OutputType.BYTES);
  }

  protected Actions createAction() {
    return new Actions(this.driver);
  }
}
