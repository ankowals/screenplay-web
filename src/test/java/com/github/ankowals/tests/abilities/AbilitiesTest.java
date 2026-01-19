package com.github.ankowals.tests.abilities;

import com.github.ankowals.framework.helpers.Try;
import com.github.ankowals.framework.screenplay.Question;
import com.github.ankowals.framework.screenplay.abilities.AssertSoftly;
import com.github.ankowals.framework.screenplay.abilities.AwaitPatiently;
import com.github.ankowals.framework.screenplay.abilities.cleanup.DoTheCleanUp;
import com.github.ankowals.framework.screenplay.abilities.memory.*;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.actor.Actors;
import com.github.ankowals.framework.screenplay.helpers.See;
import com.github.ankowals.framework.screenplay.helpers.use.UseAbility;
import java.time.Duration;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junitpioneer.jupiter.DisableIfTestFails;

@DisableIfTestFails
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AbilitiesTest {

  Actor actor;

  @BeforeAll
  void beforeAll() {
    this.actor = Actors.withAbilities();
    this.actor.can(
        AwaitPatiently.with(Awaitility.await().ignoreExceptions().atMost(Duration.ofSeconds(3))));
  }

  @AfterEach
  void afterEach() {
    DoTheCleanUp.as(this.actor).runAll();
  }

  @Test
  @Order(1)
  void shouldRememberThings() throws Exception {
    UseAbility.of(this.actor)
        .to(DoTheCleanUp.class)
        .onTeardownActions()
        .add(() -> Forget.everything().performAs(this.actor));

    this.actor.attemptsTo(RememberThat.valueOf("message").is("Do nothing"));
    String answer = this.actor.asksFor(TheRemembered.valueOf("message", String.class));

    this.actor.should(See.that(TheActual.value(answer), Matchers.is("Do nothing")));

    this.actor.should(
        See.that(
            TheRemembered.valueOf("terefere", String.class, Or.empty()),
            Matchers.is(Matchers.nullValue())));

    Memory.Key<String> key = new Memory.Key<>("terefere", String.class);

    this.actor.should(
        See.that(
            TheRemembered.valueOf(key, Or.askFor(TheRemembered.valueOf("message", String.class))),
            Matchers.equalTo("Do nothing")));

    this.actor.attemptsTo(RememberThat.valueOf(key).is("terefere"));
    this.actor.should(See.that(TheRemembered.valueOf(key), Matchers.is("terefere")));
    this.actor.attemptsTo(Forget.valueOf(key));

    answer = this.actor.asksFor(TheRemembered.valueOf(key, Or.value("hopsiaisa")));
    this.actor.should(See.that(TheActual.value(answer), Matchers.is("hopsiaisa")));

    this.actor.attemptsTo(RememberThat.valueOf("srututu").is(TheRemembered.valueOf(key)));
    this.actor.should(
        See.that(TheRemembered.valueOf("srututu", String.class), Matchers.is("hopsiaisa")));
  }

  @Test
  @Order(2)
  void shouldForgetEverything() {
    Assertions.assertThatThrownBy(
            () -> TheRemembered.valueOf("message", String.class).answeredBy(this.actor))
        .hasMessage(
            "Actor does not recall [message]. Call remember() first to define this object in Actor's memory.");
  }

  @Test
  @Order(3)
  void shouldAssertSoftly() {
    String actual =
        Try.failable(() -> TheRemembered.valueOf("message", String.class).answeredBy(this.actor))
            .orElse(
                () -> {
                  this.actor.attemptsTo(RememberThat.valueOf("message").is("Do nothing"));
                  return TheRemembered.valueOf("message", String.class).answeredBy(this.actor);
                });

    UseAbility.of(this.actor)
        .to(AssertSoftly.class)
        .that(softly -> softly.assertThat(actual).isEqualTo("Do nothing"));
  } // NOSONAR: assert all is called by ability

  @Test
  @Order(4)
  void shouldEventuallyEvaluateConsequence() {
    Assertions.assertThatExceptionOfType(ConditionTimeoutException.class)
        .isThrownBy( // NOSONAR
            () ->
                this.actor.should(
                    See.eventually(
                        TheRemembered.valueOf("message", String.class), Matchers.is("terefere"))));
  }

  @Test
  @Order(5)
  void shouldEventuallyAssertQuestion() {
    Assertions.assertThatExceptionOfType(ConditionTimeoutException.class)
        .isThrownBy( // NOSONAR
            () ->
                this.actor.should(
                    See.eventually(
                        TheRemembered.valueOf("message", String.class),
                        msg -> Assertions.assertThat(msg).isEqualTo("terefere"))));
  }

  @Test
  @Order(6)
  void shouldEventuallyAssert() {
    Assertions.assertThatExceptionOfType(ConditionTimeoutException.class)
        .isThrownBy( // NOSONAR
            () ->
                this.actor.should(
                    See.eventually(
                        () -> Assertions.assertThat(new Object()).isEqualTo("terefere"))));
  }

  @Test
  @Order(7)
  void shouldShallowCopyMemory() throws Exception {
    Actor otherActor = Actors.withAbilities();

    this.actor.attemptsTo(RememberThat.valueOf("word").is("Tequila123"));
    this.actor.attemptsTo(RememberThat.valueOf("number").is(123));

    Assertions.assertThatExceptionOfType(NoObjectToRecallException.class)
        .isThrownBy( // NOSONAR
            () -> otherActor.asksFor(TheRemembered.valueOf("word", String.class)))
        .withMessage(
            "Actor does not recall [word]. Call remember() first to define this object in Actor's memory.");

    Memory memory = this.actor.asksFor(TheRemembered.memories());
    otherActor.can(RememberThings.with(memory));

    otherActor.should(
        See.that(TheRemembered.valueOf("word", String.class), Matchers.equalTo("Tequila123")));
    otherActor.should(
        See.that(TheRemembered.valueOf("number", Integer.class), Matchers.equalTo(123)));
  }

  private static class TheActual {
    private static <T> Question<T> value(T value) {
      return actor -> value;
    }
  }
}
