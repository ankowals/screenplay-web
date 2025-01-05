package framework.web.pom.elements.angular;

import framework.web.pom.conditions.AngularExpectedConditions;
import framework.web.pom.elements.Input;
import framework.web.pom.elements.common.InputImpl;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AngularInputImpl extends InputImpl implements Input {

  private final WebDriverWait webDriverWait;

  public AngularInputImpl(WebElement webElement, WebDriverWait webDriverWait) {
    super(webElement);
    this.webDriverWait = webDriverWait;
  }

  @Override
  public void clear() {
    super.clear();
    this.waitUntil(AngularExpectedConditions.contentLoaded());
  }

  @Override
  public void insert(String text) {
    super.insert(text);
    this.waitUntil(AngularExpectedConditions.contentLoaded());
  }

  @Override
  public String getText() {
    return super.getAttribute("value");
  }

  public static AngularInputImpl of(WebElement webElement, WebDriverWait webDriverWait) {
    return new AngularInputImpl(webElement, webDriverWait);
  }

  private void waitUntil(List<ExpectedCondition<?>> conditions) {
    conditions.forEach(this.webDriverWait::until);
  }
}
