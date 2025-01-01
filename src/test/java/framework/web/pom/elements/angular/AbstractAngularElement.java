package framework.web.pom.elements.angular;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractAngularElement {
  protected final WebElement webElement;
  protected final WebDriverWait webDriverWait;

  protected AbstractAngularElement(WebElement webElement, WebDriverWait webDriverWait) {
    this.webElement = webElement;
    this.webDriverWait = webDriverWait;
  }

  protected void waitUntil(List<ExpectedCondition<?>> conditions) {
    conditions.forEach(this.webDriverWait::until);
  }
}
