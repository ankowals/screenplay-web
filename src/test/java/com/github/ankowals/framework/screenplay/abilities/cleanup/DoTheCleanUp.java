package com.github.ankowals.framework.screenplay.abilities.cleanup;

import com.github.ankowals.framework.screenplay.Ability;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.helpers.use.UseAbility;

public record DoTheCleanUp(OnTeardownActions onTeardownActions) implements Ability {
  public static OnTeardownActions as(Actor actor) {
    return UseAbility.of(actor).to(DoTheCleanUp.class).onTeardownActions();
  }

  public static DoTheCleanUp with(OnTeardownActions onTeardownActions) {
    return new DoTheCleanUp(onTeardownActions);
  }
}
