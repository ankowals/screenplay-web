package tests.screenplay;

import framework.helpers.Try;
import framework.screenplay.abilities.AssertSoftly;
import framework.screenplay.abilities.cleanup.DoTheCleanUp;
import framework.screenplay.abilities.cleanup.OnTeardownActions;
import framework.screenplay.abilities.memory.*;
import framework.screenplay.abilities.use.UseAbility;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.See;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

class ScreenplayAbilitiesTest extends ScreenplayTestBase {

  Actor actor;

  @BeforeAll
  void beforeAll() {
    this.actor = new Actor();
    this.actor.can(
        RememberThings.with(new Memory()),
        DoTheCleanUp.with(new OnTeardownActions()),
        AssertSoftly.with(new SoftAssertions()));
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
    this.actor.should(See.that(TheValue.rememberedAs("message", String.class))).isNotNull();
  }

  @Test
  @Order(2)
  void shouldForgetEverything() {
    Assertions.assertThatThrownBy(
            () -> TheValue.rememberedAs("message", String.class).answeredBy(this.actor))
        .hasMessage(
            "Actor does not recall [message]. Call remember() first to define this object in Actor's memory.");
  }

  @Test
  @Order(3)
  void shouldAssertSoftly() {
    String actual =
        Try.failable(() -> TheValue.rememberedAs("message", String.class).answeredBy(this.actor))
            .orElse(
                () -> {
                  this.actor.attemptsTo(RememberThat.valueOf("message").is("Do nothing"));
                  return TheValue.rememberedAs("message", String.class).answeredBy(this.actor);
                });

    UseAbility.of(this.actor)
        .to(AssertSoftly.class)
        .that(softly -> softly.assertThat(actual).isEqualTo("Do nothing"));
  } // NOSONAR: assert all is called by ability
}
