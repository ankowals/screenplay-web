package tests;

import framework.screenplay.abilities.AssertSoftly;
import framework.screenplay.abilities.cleanup.DoTheCleanUp;
import framework.screenplay.abilities.cleanup.OnTeardownActions;
import framework.screenplay.abilities.memory.*;
import framework.screenplay.abilities.use.UseAbility;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.See;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;

/*
  used to build test chaining (http://xunitpatterns.com/Chained%20Tests.html),
  useful if multiple actors should be involved in a complex test
*/
@DisableIfTestFails
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ScreenplayDemoTest {

  Actor actor;
  SoftAssertions softAssertions;

  @BeforeEach
  void setup() {
    Memory sharedMemory = new Memory();
    this.softAssertions = new SoftAssertions();

    this.actor = new Actor();
    this.actor.can(RememberThings.with(sharedMemory), DoTheCleanUp.with(new OnTeardownActions()), AssertSoftly.with(this.softAssertions));
  }

  @AfterEach
  void teardown() {
    System.out.println("After each hook starts");
    DoTheCleanUp.as(this.actor).runAll();
  }

  @Test
  @Order(1)
  void shouldRememberThings() throws Exception {
    UseAbility.of(this.actor)
        .to(DoTheCleanUp.class)
        .onTeardownActions()
        .add(() -> System.out.println("Print it on test teardown"));

    this.actor.attemptsTo(RememberThat.valueOf("otherActor").is(new Actor()));
    this.actor.should(See.that(Remembered.valueOf("otherActor", Actor.class))).isNotNull();
  }

  @Test
  @Order(2)
  void shouldRunActionAsBetterActor() throws Exception {
    this.actor.attemptsTo(RememberThat.valueOf("message").is("Do nothing"));

    String actual = Remembered.valueOf("message", String.class).answeredBy(this.actor);

    UseAbility.of(this.actor)
        .to(AssertSoftly.class)
        .that(softly -> softly.assertThat(actual).isEqualTo("Do nothing"));
  }
}
