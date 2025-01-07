package framework.screenplay.abilities.cleanup;

import framework.screenplay.Ability;
import framework.screenplay.abilities.UseAbility;
import framework.screenplay.actor.Actor;

public record DoTheCleanUp(OnTeardownActions onTeardownActions) implements Ability {
  public static OnTeardownActions as(Actor actor) {
    return UseAbility.of(actor).to(DoTheCleanUp.class).onTeardownActions();
  }

  public static DoTheCleanUp with(OnTeardownActions onTeardownActions) {
    return new DoTheCleanUp(onTeardownActions);
  }
}
