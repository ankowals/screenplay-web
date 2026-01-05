package framework.screenplay.actor.use;

import framework.screenplay.Ability;
import framework.screenplay.actor.Actor;

public class UseAbility {

  private final Actor actor;

  private UseAbility(Actor actor) {
    this.actor = actor;
  }

  public static UseAbility of(Actor actor) {
    return new UseAbility(actor);
  }

  public <T extends Ability> T to(Class<T> doSomething) {
    T ability = this.actor.usingAbilityTo(doSomething);

    if (ability == null) {
      throw new NoMatchingAbilityException(doSomething);
    }

    return ability;
  }
}
