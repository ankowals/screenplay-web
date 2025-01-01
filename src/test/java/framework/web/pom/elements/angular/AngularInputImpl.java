package framework.web.pom.elements.angular;

import framework.web.pom.conditions.AngularExpectedConditions;
import framework.web.pom.elements.Input;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AngularInputImpl extends AbstractAngularElement implements Input {

  public AngularInputImpl(WebElement webElement, WebDriverWait webDriverWait) {
    super(webElement, webDriverWait);
  }

  @Override
  public void clear() {
    this.webElement.clear();
    this.waitUntil(AngularExpectedConditions.contentLoaded());
  }

  @Override
  public void insert(String text) {
    this.webElement.clear();
    this.webElement.sendKeys(text);
    this.waitUntil(AngularExpectedConditions.contentLoaded());
  }

  @Override
  public String getText() {
    return this.webElement.getAttribute("value");
  }

  public static AngularInputImpl of(WebElement webElement, WebDriverWait webDriverWait) {
    return new AngularInputImpl(webElement, webDriverWait);
  }
}
