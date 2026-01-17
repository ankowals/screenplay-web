package com.github.ankowals.framework.screenplay.abilities.memory;

import com.github.ankowals.framework.screenplay.Ability;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.helpers.use.UseAbility;

public record RememberThings(Memory memory) implements Ability {
  public static Memory as(Actor actor) {
    return UseAbility.of(actor).to(RememberThings.class).memory();
  }

  public static RememberThings with(Memory memory) {
    return new RememberThings(memory);
  }
}
