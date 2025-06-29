package framework.web.pom.page;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import org.openqa.selenium.WebDriver;

public class PageFactory {

  private final WebDriver driver;

  public PageFactory(WebDriver driver) {
    this.driver = Objects.requireNonNull(driver);
  }

  public WebDriver usingBrowser() {
    return this.driver;
  }

  public <T extends BasePage> T onPage(Class<T> clazz) {
    try {
      return this.init(clazz);
    } catch (NoSuchMethodException
        | InvocationTargetException
        | InstantiationException
        | IllegalAccessException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private <T extends BasePage> T init(Class<T> type)
      throws NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {
    return type.getConstructor(WebDriver.class).newInstance(this.driver);
  }
}
