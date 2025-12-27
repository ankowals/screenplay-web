package framework.screenplay.abilities;

import framework.screenplay.Ability;
import framework.screenplay.abilities.use.UseAbility;
import framework.screenplay.actor.Actor;
import org.awaitility.core.ConditionFactory;

public record AwaitPatiently(ConditionFactory conditionFactory) implements Ability {
  public static ConditionFactory as(Actor actor) {
    return UseAbility.of(actor).to(AwaitPatiently.class).conditionFactory();
  }

  public static AwaitPatiently with(ConditionFactory conditionFactory) {
    return new AwaitPatiently(conditionFactory);
  }
}
