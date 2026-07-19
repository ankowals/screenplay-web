package com.github.ankowals.framework.screenplay.helpers;

import com.github.ankowals.framework.screenplay.Consequence;
import com.github.ankowals.framework.screenplay.Question;
import com.github.ankowals.framework.screenplay.abilities.AwaitPatiently;
import com.github.ankowals.framework.screenplay.helpers.use.UseAbility;
import java.util.Arrays;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.stream.Streams;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.HamcrestCondition;
import org.awaitility.core.ThrowingRunnable;
import org.hamcrest.Matcher;

public class See {

  /**
   * then(actor).should(See.that(TheRemembered.valueOf("customerId", String.class),
   * Matchers.is("Tequila123")))
   */
  @SafeVarargs
  public static <T> Consequence that(Question<T> question, Matcher<? super T>... matchers) {
    return actor -> {
      T answer = actor.asksFor(question);
      Streams.failableStream(Arrays.stream(matchers))
          .forEach(
              matcher -> Assertions.assertThat(answer).is(HamcrestCondition.matching(matcher)));
    };
  }

  /**
   * then(actor).should(See.that(TheRemembered.valueOf("customerId", String.class), result ->
   * Assertions.assertThat(result).isEqualTo("Tequila123")))
   */
  public static <T> Consequence that(
      Question<T> question, FailableConsumer<? super T, Exception> assertConsumer) {
    return actor -> assertConsumer.accept(actor.asksFor(question));
  }

  public static <T> Consequence eventuallyThat(Question<T> question, Matcher<? super T> matcher) {
    return actor ->
        UseAbility.of(actor)
            .to(AwaitPatiently.class)
            .conditionFactory()
            .untilAsserted(() -> actor.should(See.that(question, matcher)));
  }

  public static <T> Consequence eventuallyThat(
      Question<T> question, FailableConsumer<? super T, Exception> assertConsumer) {
    return actor ->
        UseAbility.of(actor)
            .to(AwaitPatiently.class)
            .conditionFactory()
            .untilAsserted(() -> actor.should(See.that(question, assertConsumer)));
  }

  /**
   * then(actor).should(See.eventuallyThat( () -> { String actual =
   * actor.asksFor(TheRemembered.valueOf("customerId", String.class));
   * Assertions.assertThat(actual).isEqualTo("Tequila123"); }
   */
  public static Consequence eventuallyThat(ThrowingRunnable throwingRunnable) {
    return actor ->
        UseAbility.of(actor)
            .to(AwaitPatiently.class)
            .conditionFactory()
            .untilAsserted(throwingRunnable);
  }
}
