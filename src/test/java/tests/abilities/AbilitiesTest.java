package tests.abilities;

import framework.helpers.Try;
import framework.screenplay.abilities.AssertSoftly;
import framework.screenplay.abilities.cleanup.DoTheCleanUp;
import framework.screenplay.abilities.memory.*;
import framework.screenplay.actor.Actor;
import framework.screenplay.actor.Actors;
import framework.screenplay.helpers.See;
import framework.screenplay.helpers.use.UseAbility;
import org.assertj.core.api.Assertions;
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
    this.actor.should(See.that(TheRemembered.valueOf("message", String.class))).isNotNull();
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
}
