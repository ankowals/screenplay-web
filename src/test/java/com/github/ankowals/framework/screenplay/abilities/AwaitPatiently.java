package com.github.ankowals.framework.screenplay.abilities;

import com.github.ankowals.framework.screenplay.Ability;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.helpers.use.UseAbility;
import org.awaitility.core.ConditionFactory;

public record AwaitPatiently(ConditionFactory conditionFactory) implements Ability {
  public static ConditionFactory as(Actor actor) {
    return UseAbility.of(actor).to(AwaitPatiently.class).conditionFactory();
  }

  public static AwaitPatiently with(ConditionFactory conditionFactory) {
    return new AwaitPatiently(conditionFactory);
  }
}
