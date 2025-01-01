package tests;

import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.DoTheCleanUp;
import framework.screenplay.helpers.See;
import framework.screenplay.helpers.UseAbility;
import framework.screenplay.hooks.OnTeardown;
import framework.screenplay.interactions.RememberThat;
import framework.screenplay.interactions.TheValue;
import org.junit.jupiter.api.*;

class ScreenplayDemoTest {

  Actor actor;

  @BeforeEach
  void setup() {
    this.actor = new Actor().can(DoTheCleanUp.with(new OnTeardown()));
  }

  @AfterEach
  void teardown() {
    System.out.println("After each hook starts");
    DoTheCleanUp.as(this.actor).run();
  }

  @Test
  void shouldRememberThings() {
    UseAbility.of(this.actor)
        .to(DoTheCleanUp.class)
        .onTeardown()
        .defer(() -> System.out.println("Print it on test teardown"));

    this.actor.attemptsTo(RememberThat.valueOf("otherActor").is(new Actor()));
    this.actor.should(See.that(TheValue.of("otherActor"))).isNotNull();
  }
}
