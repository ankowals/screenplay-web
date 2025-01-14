package tests.screenplay;

import framework.screenplay.abilities.memory.Memory;
import framework.screenplay.abilities.memory.RememberThat;
import framework.screenplay.abilities.memory.RememberThings;
import framework.screenplay.abilities.memory.TheValue;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.See;
import framework.screenplay.helpers.task.Task;
import framework.screenplay.helpers.task.TestIdentifier;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;

class ScreenplayTasksTest extends ScreenplayTestBase {

  Task task;
  Actor actor;

  @BeforeAll
  void beforeAll() {
    this.actor = new Actor();
    this.task = new Task(EXTENT_REPORT_EXTENSION);
  }

  @Test
  @Order(1)
  void shouldRunTasks(TestInfo testInfo) throws Exception {
    Customer customer = new Customer("Tequila123");

    this.task.where(
        new TestIdentifier(testInfo),
        "Actor remembers things",
        () -> {
          Device virualDevice = Devices.virtualDevice();
          this.actor.can(RememberThings.with(new Memory()));
          this.actor.attemptsTo(RememberThat.valueOf("device").is(virualDevice));
          this.actor.attemptsTo(RememberThat.valueOf("customer").is(customer));
        });

    this.task.where(
        new TestIdentifier(testInfo),
        "Actor asserts things",
        () -> {
          this.actor.should(See.that(TheValue.rememberedAs("device", Device.class))).isNotNull();
          this.actor
              .should(See.that(TheValue.rememberedAs("customer", Customer.class)))
              .returns("Tequila123", Customer::name);
        });
  }

  static class Devices {
    static Device virtualDevice() {
      return new Device(RandomStringUtils.insecure().nextAlphabetic(8));
    }
  }

  record Device(String name) {}

  record Customer(String name) {}
}
