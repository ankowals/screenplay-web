package framework.screenplay.actor;

import framework.screenplay.abilities.AssertSoftly;
import framework.screenplay.abilities.AwaitPatiently;
import framework.screenplay.abilities.cleanup.DoTheCleanUp;
import framework.screenplay.abilities.cleanup.OnTeardownActions;
import framework.screenplay.abilities.memory.Memory;
import framework.screenplay.abilities.memory.RememberThings;
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
