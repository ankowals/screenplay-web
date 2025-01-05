package tests;

import framework.screenplay.actor.Actor;
import framework.screenplay.actor.UseAbility;
import framework.screenplay.assertions.AssertSoftly;
import framework.screenplay.cleanup.DoTheCleanUp;
import framework.screenplay.cleanup.OnTeardownActions;
import framework.screenplay.helpers.See;
import framework.screenplay.memory.*;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

class ScreenplayDemoTest {

  Actor actor;

  @BeforeEach
  void setup() {
    this.actor = new Actor();
    this.actor.can(
        DoTheCleanUp.with(new OnTeardownActions()),
        RememberThings.with(new Memory()),
        AssertSoftly.with(new SoftAssertions()));
  }

  @AfterEach
  void teardown() {
    System.out.println("After each hook starts");
    DoTheCleanUp.as(this.actor).runAll();
  }

  @Test
  void shouldRememberThings() {
    UseAbility.of(this.actor)
        .to(DoTheCleanUp.class)
        .onTeardownActions()
        .add(() -> System.out.println("Print it on test teardown"));

    this.actor.attemptsTo(RememberThat.valueOf("otherActor").is(new Actor()));
    this.actor.should(See.that(Remembered.valueOf("otherActor"))).isNotNull();
  }
}
