package framework.web.pom.page;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseView {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
  protected final WebDriver driver;
  protected final WebDriverWait wait;

  public BaseView(WebDriver driver) {
    this.driver = driver;
    this.wait = this.createWebDriverWait(DEFAULT_TIMEOUT);
  }

  protected WebDriverWait createWebDriverWait(Duration timeout) {
    return new WebDriverWait(this.driver, timeout);
  }

  protected WebDriverWait createWebDriverWait(Duration timeout, Duration sleep) {
    return new WebDriverWait(this.driver, timeout, sleep);
  }

  protected void waitUntil(ExpectedCondition<?>... conditions) {
    this.waitUntil(Arrays.asList(conditions));
  }

  protected void waitUntil(List<ExpectedCondition<?>> conditions) {
    conditions.forEach(this.wait::until);
  }
}
