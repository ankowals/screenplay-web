package framework.screenplay.actor;

import framework.screenplay.Ability;

public interface ManagesAbilities {
  <T extends Ability> ManagesAbilities can(T doSomething);

  <T extends Ability> T usingAbilityTo(Class<? extends T> doSomething);
}
