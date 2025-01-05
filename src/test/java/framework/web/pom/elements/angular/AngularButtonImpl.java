package framework.web.pom.elements.angular;

import framework.web.pom.conditions.AngularExpectedConditions;
import framework.web.pom.elements.Button;
import framework.web.pom.elements.common.ButtonImpl;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

@SuppressWarnings("NullableProblems")
public class AngularButtonImpl extends ButtonImpl implements Button {

  private final WebDriverWait webDriverWait;

  public AngularButtonImpl(WebElement webElement, WebDriverWait webDriverWait) {
    super(webElement);
    this.webDriverWait = webDriverWait;
  }

  @Override
  public void click() {
    super.click();
    this.waitUntil(AngularExpectedConditions.contentLoaded());
  }

  @Override
  public String getText() {
    return super.getAttribute("title");
  }

  public static AngularButtonImpl of(WebElement webElement, WebDriverWait webDriverWait) {
    return new AngularButtonImpl(webElement, webDriverWait);
  }

  private void waitUntil(List<ExpectedCondition<?>> conditions) {
    conditions.forEach(this.webDriverWait::until);
  }
}
