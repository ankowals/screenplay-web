package framework.web.pom.elements.common;

import framework.web.pom.elements.Input;
import org.openqa.selenium.*;

public class InputImpl implements Input {

  private final WebElement webElement;

  public InputImpl(WebElement webElement) {
    this.webElement = webElement;
  }

  @Override
  public void clear() {
    this.webElement.clear();
  }

  @Override
  public void insert(String text) {
    this.clear();
    this.webElement.sendKeys(text);
  }

  @Override
  public String getText() {
    return this.webElement.getAttribute("value");
  }

  public static InputImpl of(WebElement webElement) {
    return new InputImpl(webElement);
  }
}
