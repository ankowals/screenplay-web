package framework.screenplay.actor;

import framework.screenplay.Ability;

public class UseAbility {

  private final Actor actor;

  private UseAbility(Actor actor) {
    this.actor = actor;
  }

  public static UseAbility of(Actor actor) {
    return new UseAbility(actor);
  }

  public <T extends Ability> T to(Class<T> doSomething) {
    return this.actor.usingAbilityTo(doSomething);
  }
}
