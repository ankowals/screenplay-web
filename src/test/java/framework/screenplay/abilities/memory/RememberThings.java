package framework.screenplay.abilities.memory;

import framework.screenplay.Ability;
import framework.screenplay.abilities.use.UseAbility;
import framework.screenplay.actor.Actor;

public record RememberThings(Memory memory) implements Ability {
  public static Memory as(Actor actor) {
    return UseAbility.of(actor).to(RememberThings.class).memory();
  }

  public static RememberThings with(Memory memory) {
    return new RememberThings(memory);
  }
}
