package framework.web.screenplay;

import framework.screenplay.Ability;
import framework.screenplay.actor.Actor;
import framework.screenplay.actor.use.UseAbility;
import framework.web.pom.page.BasePage;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import org.openqa.selenium.WebDriver;

public record BrowseTheWeb(WebDriver driver) implements Ability {
  public static BrowseTheWeb with(WebDriver driver) {
    return new BrowseTheWeb(driver);
  }

  public static PageFactory as(Actor actor) {
    return new PageFactory(UseAbility.of(actor).to(BrowseTheWeb.class).driver());
  }

  public static class PageFactory {

    private final WebDriver driver;

    PageFactory(WebDriver driver) {
      this.driver = Objects.requireNonNull(driver);
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
}
