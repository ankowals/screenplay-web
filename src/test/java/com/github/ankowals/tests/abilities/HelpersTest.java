package com.github.ankowals.tests.abilities;

import com.github.ankowals.framework.screenplay.abilities.memory.RememberThat;
import com.github.ankowals.framework.screenplay.abilities.memory.TheRemembered;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.actor.Actors;
import com.github.ankowals.framework.screenplay.helpers.See;
import com.github.ankowals.framework.screenplay.helpers.parallel.InParallel;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;

@DisableIfTestFails
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HelpersTest {

  @Test
  @Order(1)
  void shouldRunParallelTasks() throws Exception {
    Actor actor = Actors.withAbilities();

    InParallel.actor(actor)
        .attemptsTo(
            RememberThat.valueOf("thread1").is(Thread.currentThread().getName()),
            RememberThat.valueOf("thread2").is(Thread.currentThread().getName()),
            RememberThat.valueOf("thread3").is(Thread.currentThread().getName()));

    actor.should(
        See.that(
            TheRemembered.valueOf("thread1", String.class), Matchers.not(Matchers.nullValue())));
    actor.should(
        See.that(
            TheRemembered.valueOf("thread3", String.class), Matchers.not(Matchers.nullValue())));
    actor.should(
        See.that(
            TheRemembered.valueOf("thread1", String.class),
            Matchers.not(
                Matchers.equalTo(
                    TheRemembered.valueOf("thread3", String.class).answeredBy(actor)))));
  }

  @Test
  @Order(2)
  void shouldRunParallelInteractionsByEachActor() {
    Actor actor1 = Actors.withAbilities();
    Actor actor2 = Actors.withAbilities();
    Actor actor3 = Actors.withAbilities();
    Actor actor4 = Actors.withAbilities();
    Actor actor5 = Actors.withAbilities();
    Actor actor6 = Actors.withAbilities();
    Actor actor7 = Actors.withAbilities();
    Actor actor8 = Actors.withAbilities();
    Actor actor9 = Actors.withAbilities();

    InParallel.actors(actor1, actor2, actor3, actor4, actor5, actor6, actor7, actor8, actor9)
        .eachAttemptsTo(() -> RememberThat.valueOf("thread").is(Thread.currentThread().getName()));

    actor1.should(
        See.that(
            TheRemembered.valueOf("thread", String.class), Matchers.not(Matchers.nullValue())));
    actor3.should(
        See.that(
            TheRemembered.valueOf("thread", String.class), Matchers.not(Matchers.nullValue())));

    actor1.should(
        See.that(
            TheRemembered.valueOf("thread", String.class),
            val ->
                Assertions.assertThat(val)
                    .isNotEqualTo(
                        TheRemembered.valueOf("thread", String.class).answeredBy(actor3))));
  }

  @Test
  @Order(3)
  void shouldRunParallelQuestions() {
    Actor actor1 = Actors.withAbilities();
    Actor actor2 = Actors.withAbilities();
    Actor actor3 = Actors.withAbilities();
    Actor actor4 = Actors.withAbilities();
    Actor actor5 = Actors.withAbilities();
    Actor actor6 = Actors.withAbilities();
    Actor actor7 = Actors.withAbilities();
    Actor actor8 = Actors.withAbilities();

    InParallel.actors(actor1, actor2, actor3, actor4, actor5, actor6, actor7, actor8)
        .eachAttemptsTo(() -> RememberThat.valueOf("thread").is(Thread.currentThread().getName()));

    List<String> threads =
        InParallel.actors(actor1, actor2, actor3, actor4, actor5, actor6, actor7, actor8)
            .eachAsksFor(() -> TheRemembered.valueOf("thread", String.class));

    Assertions.assertThat(threads).isNotEmpty().hasSize(8);
    Assertions.assertThat(threads.getFirst()).isNotEqualTo(threads.getLast());
  }
}
