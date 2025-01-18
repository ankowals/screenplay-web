package tests.screenplay;

import framework.screenplay.abilities.memory.Memory;
import framework.screenplay.abilities.memory.RememberThat;
import framework.screenplay.abilities.memory.RememberThings;
import framework.screenplay.abilities.memory.TheValue;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.InParallel;
import framework.screenplay.helpers.See;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

class ScreenplayTasksTest extends ScreenplayTestBase {

  Actor actor;

  @BeforeAll
  void beforeAll() {
    this.actor = new Actor();
  }

  @Test
  @Order(1)
  void shouldRunTasks(TestInfo testInfo) throws Exception {
    this.task.where(
        this.toTestIdentifier(testInfo),
        "Actor remembers things",
        () -> {
          this.actor.can(RememberThings.with(new Memory()));
          this.actor.attemptsTo(RememberThat.valueOf("device").is(Devices.virtualDevice()));
          this.actor.attemptsTo(RememberThat.valueOf("customer").is(Customers.tequila()));
        });

    this.task.where(
        this.toTestIdentifier(testInfo),
        "Actor asserts things",
        () -> {
          this.actor.should(See.that(TheValue.rememberedAs("device", Device.class))).isNotNull();
          this.actor
              .should(See.that(TheValue.rememberedAs("customer", Customer.class)))
              .returns("Tequila123", Customer::name);
        });
  }

  @Test
  @Order(2)
  void shouldRunParallelTasks() throws Exception {
    this.actor.can(RememberThings.with(new Memory()));

    InParallel.perform(
        () ->
            RememberThat.valueOf("thread1")
                .is(Thread.currentThread().getName())
                .performAs(this.actor),
        () ->
            RememberThat.valueOf("thread2")
                .is(Thread.currentThread().getName())
                .performAs(this.actor),
        () ->
            RememberThat.valueOf("thread3")
                .is(Thread.currentThread().getName())
                .performAs(this.actor));

    this.actor.should(See.that(TheValue.rememberedAs("thread1", String.class))).isNotNull();
    this.actor.should(See.that(TheValue.rememberedAs("thread3", String.class))).isNotNull();
    this.actor
        .should(See.that(TheValue.rememberedAs("thread1", String.class)))
        .isNotEqualTo(TheValue.rememberedAs("thread3", String.class).answeredBy(this.actor));
  }

  static class Devices {
    static Device virtualDevice() {
      return new Device(RandomStringUtils.insecure().nextAlphabetic(8));
    }
  }

  static class Customers {
    static Customer tequila() {
      return new Customer("Tequila123");
    }
  }

  record Device(String name) {}

  record Customer(String name) {}
}
