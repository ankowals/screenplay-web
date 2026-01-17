package com.github.ankowals.framework.web.pom.page;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

// can be used to return web elements of a page or conditions
public abstract class BaseView extends AbstractPage {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

  protected final WebDriverWait wait;

  public BaseView(WebDriver driver) {
    super(driver);
    this.wait = this.webDriverWait(DEFAULT_TIMEOUT);
  }

  protected WebDriverWait webDriverWait(Duration timeout) {
    return new WebDriverWait(this.driver, timeout);
  }

  protected void waitUntil(ExpectedCondition<?>... conditions) {
    this.waitUntil(Arrays.asList(conditions));
  }

  protected void waitUntil(List<ExpectedCondition<?>> conditions) {
    conditions.forEach(this.wait::until);
  }
}
