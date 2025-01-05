package framework.screenplay.cleanup;

import framework.screenplay.Ability;
import framework.screenplay.actor.Actor;

public record DoTheCleanUp(OnTeardownActions onTeardownActions) implements Ability {
  public static OnTeardownActions as(Actor actor) {
    return actor.usingAbilityTo(DoTheCleanUp.class).onTeardownActions();
  }

  public static DoTheCleanUp with(OnTeardownActions onTeardownActions) {
    return new DoTheCleanUp(onTeardownActions);
  }
}
