package com.github.ankowals.domain;

import com.github.ankowals.framework.screenplay.Ability;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.helpers.use.UseAbility;
import io.github.bonigarcia.wdm.WebDriverManager;

public record ManageBrowsers(WebDriverManager webDriverManager) implements Ability {
  public static ManageBrowsers with(WebDriverManager webDriverManager) {
    return new ManageBrowsers(webDriverManager);
  }

  public static WebDriverManager as(Actor actor) {
    return UseAbility.of(actor).to(ManageBrowsers.class).webDriverManager();
  }
}
