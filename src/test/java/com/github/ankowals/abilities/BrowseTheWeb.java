package com.github.ankowals.abilities;

import com.github.ankowals.framework.screenplay.Ability;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.helpers.use.UseAbility;
import com.github.ankowals.framework.web.pom.page.BasePage;
import java.util.Objects;
import java.util.function.Function;
import org.openqa.selenium.WebDriver;

public record BrowseTheWeb(WebDriver webDriver) implements Ability {
  public static BrowseTheWeb with(WebDriver webDriver) {
    return new BrowseTheWeb(webDriver);
  }

  public static PageFactory as(Actor actor) {
    return new PageFactory(UseAbility.of(actor).to(BrowseTheWeb.class).webDriver());
  }

  public static class PageFactory {

    private final WebDriver driver;

    PageFactory(WebDriver driver) {
      this.driver = Objects.requireNonNull(driver);
    }

    public <T extends BasePage> T on(Function<WebDriver, T> mapper) {
      return mapper.apply(this.driver);
    }
  }
}
