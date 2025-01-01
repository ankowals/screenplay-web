package framework.screenplay.helpers;

import framework.screenplay.Ability;
import framework.screenplay.actor.Actor;

public class UseAbility {

  private final Actor actor;

  public UseAbility(Actor actor) {
    this.actor = actor;
  }

  public static UseAbility of(Actor actor) {
    return new UseAbility(actor);
  }

  public <T extends Ability> T to(Class<T> doSomething) {
    return this.actor.usingAbilityTo(doSomething);
  }
}
