package tests.screenplay;

import framework.screenplay.abilities.memory.Memory;
import framework.screenplay.abilities.memory.RememberThat;
import framework.screenplay.abilities.memory.RememberThings;
import framework.screenplay.abilities.memory.TheValue;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.InParallel;
import framework.screenplay.helpers.See;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class ScreenplayTasksTest extends ScreenplayTestBase {

  Actor actor;

  @BeforeAll
  void beforeAll() {
    this.actor = new Actor();
  }

  @Test
  @Order(1)
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

    actor1.should(See.that(TheValue.rememberedAs("thread", String.class))).isNotNull();
    actor3.should(See.that(TheValue.rememberedAs("thread", String.class))).isNotNull();

    actor1
        .should(See.that(TheValue.rememberedAs("thread", String.class)))
        .isNotEqualTo(TheValue.rememberedAs("thread", String.class).answeredBy(actor3));
  }

  @Test
  @Order(3)
  void shouldRunParallelQuestions() {
    Actor actor1 = Actors.withMemory();
    Actor actor2 = Actors.withMemory();
    Actor actor3 = Actors.withMemory();

    InParallel.actors(actor1, actor2, actor3)
        .eachAttemptsTo(() -> RememberThat.valueOf("thread").is(Thread.currentThread().getName()));

    List<String> threads =
        InParallel.performAndGet(
            () -> TheValue.rememberedAs("thread", String.class).answeredBy(actor1),
            () -> TheValue.rememberedAs("thread", String.class).answeredBy(actor2),
            () -> TheValue.rememberedAs("thread", String.class).answeredBy(actor3));

    Assertions.assertThat(threads).isNotEmpty().hasSize(3);
    Assertions.assertThat(threads.getFirst()).isNotEqualTo(threads.getLast());
  }

  static class Actors {
    static Actor withMemory() {
      Actor anActor = new Actor();
      anActor.can(RememberThings.with(new Memory()));

      return anActor;
    }
  }
}
