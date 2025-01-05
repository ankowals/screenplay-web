package framework.screenplay.actor;

import framework.screenplay.Ability;

public interface ManagesAbilities {
  <T extends Ability> void can(T... doSomething);

  <T extends Ability> T usingAbilityTo(Class<? extends T> doSomething);
}
