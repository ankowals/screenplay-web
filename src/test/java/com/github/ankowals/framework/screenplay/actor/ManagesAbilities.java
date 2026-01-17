package com.github.ankowals.framework.screenplay.actor;

import com.github.ankowals.framework.screenplay.Ability;

public interface ManagesAbilities {
  <T extends Ability> void can(T... doSomething);

  <T extends Ability> T usingAbilityTo(Class<T> doSomething);
}
