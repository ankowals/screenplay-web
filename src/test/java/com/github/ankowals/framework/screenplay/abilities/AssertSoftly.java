package com.github.ankowals.framework.screenplay.abilities;

import com.github.ankowals.framework.screenplay.Ability;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.helpers.use.UseAbility;
import java.util.function.Consumer;
import org.assertj.core.api.SoftAssertions;

public record AssertSoftly(SoftAssertions softAssertions) implements Ability {
  public static SoftAssertions as(Actor actor) {
    return UseAbility.of(actor).to(AssertSoftly.class).softAssertions();
  }

  public static AssertSoftly with(SoftAssertions softAssertions) {
    return new AssertSoftly(softAssertions);
  }

  public void that(Consumer<SoftAssertions> softly) {
    SoftAssertions.assertSoftly(softly);
  }
}
