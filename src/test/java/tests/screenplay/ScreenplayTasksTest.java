package tests.screenplay;

import framework.helpers.InParallel;
import framework.screenplay.abilities.memory.Memory;
import framework.screenplay.abilities.memory.RememberThat;
import framework.screenplay.abilities.memory.RememberThings;
import framework.screenplay.abilities.memory.TheRemembered;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.See;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class ScreenplayTasksTest extends ScreenplayTestBase {

  @Test
  @Order(1)
  void shouldRunParallelTasks() throws Exception {
    Actor actor = Actors.withMemory();

    InParallel.perform(
        () -> RememberThat.valueOf("thread1").is(Thread.currentThread().getName()).performAs(actor),
        () -> RememberThat.valueOf("thread2").is(Thread.currentThread().getName()).performAs(actor),
        () ->
            RememberThat.valueOf("thread3").is(Thread.currentThread().getName()).performAs(actor));

    actor.should(See.that(TheRemembered.value("thread1", String.class))).isNotNull();
    actor.should(See.that(TheRemembered.value("thread3", String.class))).isNotNull();
    actor
        .should(See.that(TheRemembered.value("thread1", String.class)))
        .isNotEqualTo(TheRemembered.value("thread3", String.class).answeredBy(actor));
  }

  @Test
  @Order(2)
  void shouldRunParallelInteractionsByEachActor() throws Exception {
    Actor actor1 = Actors.withMemory();
    Actor actor2 = Actors.withMemory();
    Actor actor3 = Actors.withMemory();
    Actor actor4 = Actors.withMemory();
    Actor actor5 = Actors.withMemory();
    Actor actor6 = Actors.withMemory();
    Actor actor7 = Actors.withMemory();
    Actor actor8 = Actors.withMemory();
    Actor actor9 = Actors.withMemory();

    InParallel.actors(actor1, actor2, actor3, actor4, actor5, actor6, actor7, actor8, actor9)
        .eachAttemptsTo(() -> RememberThat.valueOf("thread").is(Thread.currentThread().getName()));

    actor1.should(See.that(TheRemembered.value("thread", String.class))).isNotNull();
    actor3.should(See.that(TheRemembered.value("thread", String.class))).isNotNull();

    actor1
        .should(See.that(TheRemembered.value("thread", String.class)))
        .isNotEqualTo(TheRemembered.value("thread", String.class).answeredBy(actor3));
  }

  @Test
  @Order(3)
  void shouldRunParallelQuestions() {
    Actor actor1 = Actors.withMemory();
    Actor actor2 = Actors.withMemory();
    Actor actor3 = Actors.withMemory();
    Actor actor4 = Actors.withMemory();
    Actor actor5 = Actors.withMemory();
    Actor actor6 = Actors.withMemory();
    Actor actor7 = Actors.withMemory();
    Actor actor8 = Actors.withMemory();

    InParallel.actors(actor1, actor2, actor3, actor4, actor5, actor6, actor7, actor8)
        .eachAttemptsTo(() -> RememberThat.valueOf("thread").is(Thread.currentThread().getName()));

    List<String> threads =
        InParallel.performAndGet(
            () -> TheRemembered.value("thread", String.class).answeredBy(actor1),
            () -> TheRemembered.value("thread", String.class).answeredBy(actor2),
            () -> TheRemembered.value("thread", String.class).answeredBy(actor3),
            () -> TheRemembered.value("thread", String.class).answeredBy(actor4),
            () -> TheRemembered.value("thread", String.class).answeredBy(actor5),
            () -> TheRemembered.value("thread", String.class).answeredBy(actor6),
            () -> TheRemembered.value("thread", String.class).answeredBy(actor7),
            () -> TheRemembered.value("thread", String.class).answeredBy(actor8));

    Assertions.assertThat(threads).isNotEmpty().hasSize(8);
    Assertions.assertThat(threads.getFirst()).isNotEqualTo(threads.getLast());
  }

  static class Actors {
    static Actor withMemory() {
      Actor actor = new Actor();
      actor.can(RememberThings.with(new Memory()));

      return actor;
    }
  }
}
