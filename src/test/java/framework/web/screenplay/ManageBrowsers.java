package framework.web.screenplay;

import framework.screenplay.Ability;
import framework.screenplay.abilities.use.UseAbility;
import framework.screenplay.actor.Actor;
import io.github.bonigarcia.wdm.WebDriverManager;

public record ManageBrowsers(WebDriverManager webDriverManager) implements Ability {
  public static ManageBrowsers with(WebDriverManager webDriverManager) {
    return new ManageBrowsers(webDriverManager);
  }

  public static WebDriverManager as(Actor actor) {
    return UseAbility.of(actor).to(ManageBrowsers.class).webDriverManager();
  }
}
