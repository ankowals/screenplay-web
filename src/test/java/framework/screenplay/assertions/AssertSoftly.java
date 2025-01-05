package framework.screenplay.assertions;

import framework.screenplay.Ability;
import framework.screenplay.actor.Actor;
import org.assertj.core.api.SoftAssertions;

public record AssertSoftly(SoftAssertions softAssertions) implements Ability {
  public static SoftAssertions as(Actor actor) {
    return actor.usingAbilityTo(AssertSoftly.class).softAssertions();
  }

  public static AssertSoftly with(SoftAssertions softAssertions) {
    return new AssertSoftly(softAssertions);
  }
}
