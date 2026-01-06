package framework.screenplay.abilities.cleanup;

import framework.screenplay.Ability;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.use.UseAbility;

public record DoTheCleanUp(OnTeardownActions onTeardownActions) implements Ability {
  public static OnTeardownActions as(Actor actor) {
    return UseAbility.of(actor).to(DoTheCleanUp.class).onTeardownActions();
  }

  public static DoTheCleanUp with(OnTeardownActions onTeardownActions) {
    return new DoTheCleanUp(onTeardownActions);
  }
}
