package com.github.ankowals.framework.screenplay.actor;

import com.github.ankowals.framework.screenplay.abilities.AssertSoftly;
import com.github.ankowals.framework.screenplay.abilities.AwaitPatiently;
import com.github.ankowals.framework.screenplay.abilities.cleanup.DoTheCleanUp;
import com.github.ankowals.framework.screenplay.abilities.cleanup.OnTeardownActions;
import com.github.ankowals.framework.screenplay.abilities.memory.Memory;
import com.github.ankowals.framework.screenplay.abilities.memory.RememberThings;
import java.time.Duration;
import org.assertj.core.api.SoftAssertions;
import org.awaitility.Awaitility;

public class Actors {
  public static Actor withAbilities() {
    Actor actor = new Actor();
    actor.can(
        RememberThings.with(new Memory()),
        DoTheCleanUp.with(new OnTeardownActions()),
        AssertSoftly.with(new SoftAssertions()),
        AwaitPatiently.with(Awaitility.await().ignoreExceptions().atMost(Duration.ofSeconds(11))));

    return actor;
  }
}
