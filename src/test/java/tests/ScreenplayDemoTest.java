package tests;

import framework.screenplay.hooks.OnTeardown;
import framework.screenplay.actor.Actor;
import framework.screenplay.interactions.RememberThat;
import framework.screenplay.helpers.See;
import framework.screenplay.interactions.TheValue;
import framework.screenplay.helpers.UseAnAbility;
import org.junit.jupiter.api.*;
import framework.screenplay.helpers.DoTheCleanUp;

class ScreenplayDemoTest {

    Actor actor;

    @BeforeEach
    void setup() {
        this.actor = new Actor().can(DoTheCleanUp.with(new OnTeardown()));
    }

    @AfterEach
    void teardown() {
        DoTheCleanUp.as(this.actor).execute();
    }

    @Test
    void shouldRememberThings() {
        UseAnAbility.of(this.actor).to(DoTheCleanUp.class)
                .onTeardown()
                .run(() -> System.out.println("Print it on test teardown"));

        this.actor.attemptsTo(RememberThat.valueOf("otherActor").is(new Actor()));
        this.actor.should(See.that(TheValue.of("otherActor"))).isNotNull();
    }
}
