package tests;

import framework.screenplay.abilities.AssertSoftly;
import framework.screenplay.abilities.cleanup.DoTheCleanUp;
import framework.screenplay.abilities.cleanup.OnTeardownActions;
import framework.screenplay.abilities.memory.*;
import framework.screenplay.abilities.use.UseAbility;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.See;
import framework.screenplay.helpers.Task;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;

@DisableIfTestFails
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ScreenplayAbilitiesTest {

  Actor actor;
  SoftAssertions softAssertions;

  @BeforeAll
  void beforeAll() {
    this.softAssertions = new SoftAssertions();

    this.actor = new Actor();
    this.actor.can(
        RememberThings.with(new Memory()),
        DoTheCleanUp.with(new OnTeardownActions()),
        AssertSoftly.with(this.softAssertions));
  }

  @AfterEach
  void teardown() {
    DoTheCleanUp.as(this.actor).runAll();
  }

  @Test
  @Order(1)
  void shouldRememberThings() throws Exception {
    UseAbility.of(this.actor)
        .to(DoTheCleanUp.class)
        .onTeardownActions()
        .add(() -> Forget.everything().performAs(this.actor));

    this.actor.attemptsTo(RememberThat.valueOf("otherActor").is(new Actor()));
    this.actor.should(See.that(Remembered.valueOf("otherActor", Actor.class))).isNotNull();
  }

  @Test
  @Order(2)
  void shouldForgetEverything() {
    Assertions.assertThatThrownBy(
            () -> Remembered.valueOf("otherActor", Actor.class).answeredBy(this.actor))
        .hasMessage(
            "Actor does not recall [otherActor]. Call remember() first to define this object in Actor's memory.");
  }

  @Test
  @Order(3)
  void shouldSoftAssert() throws Exception {
    this.actor.attemptsTo(RememberThat.valueOf("message").is("Do nothing"));

    String actual = Remembered.valueOf("message", String.class).answeredBy(this.actor);

    UseAbility.of(this.actor)
        .to(AssertSoftly.class)
        .that(softly -> softly.assertThat(actual).isEqualTo("Do nothing"));
  }

  @Test
  @Order(4)
  void shouldRunTasks() throws Exception {
    Actor taskActor = new Actor();
    Customer customer = new Customer("Tequila123");

    Task.where(
        "Actor remembers things",
        () -> {
          Device virualDevice = Devices.virtualDevice();
          taskActor.can(RememberThings.with(new Memory()));
          taskActor.attemptsTo(RememberThat.valueOf("device").is(virualDevice));
          taskActor.attemptsTo(RememberThat.valueOf("customer").is(customer));
        });

    Task.where(
        "Actor asserts things",
        () -> {
          taskActor.should(See.that(Remembered.valueOf("device", Device.class))).isNotNull();
          taskActor
              .should(See.that(Remembered.valueOf("customer", Customer.class)))
              .returns("Tequila123", Customer::name);
        });
  }

  @Test
  @Order(5)
  void shouldRunTasksInteractions() throws Exception {
    Actor taskActor = new Actor();

    Task.where(
            "Actor remembers things",
            actor1 -> {
              actor1.can(RememberThings.with(new Memory()));
              actor1.attemptsTo(RememberThat.valueOf("device").is(Devices.virtualDevice()));
            })
        .performAs(taskActor);

    Task.where(
            "Actor asserts things",
            actor1 ->
                actor1.should(See.that(Remembered.valueOf("device", Device.class))).isNotNull())
        .performAs(taskActor);
  }

  static class Devices {
    static Device virtualDevice() {
      return new Device(RandomStringUtils.insecure().nextAlphabetic(8));
    }
  }

  record Device(String name) {}

  record Customer(String name) {}
}
