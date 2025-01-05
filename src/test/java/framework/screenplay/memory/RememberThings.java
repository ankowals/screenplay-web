package framework.screenplay.memory;

import framework.screenplay.Ability;
import framework.screenplay.actor.Actor;

public record RememberThings(Memory memory) implements Ability {
  public static Memory as(Actor actor) {
    return actor.usingAbilityTo(RememberThings.class).memory();
  }

  public static RememberThings with(Memory memory) {
    return new RememberThings(memory);
  }
}
