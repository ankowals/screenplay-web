package framework.web.screenplay;

import framework.screenplay.Ability;
import framework.screenplay.abilities.use.UseAbility;
import framework.screenplay.actor.Actor;
import framework.web.pom.page.PageFactory;
import org.openqa.selenium.WebDriver;

public record BrowseTheWeb(WebDriver driver) implements Ability {
  public static BrowseTheWeb with(WebDriver driver) {
    return new BrowseTheWeb(driver);
  }

  public static PageFactory as(Actor actor) {
    return new PageFactory(UseAbility.of(actor).to(BrowseTheWeb.class).driver());
  }
}
