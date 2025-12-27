package framework.screenplay.helpers;

import framework.screenplay.Consequence;
import framework.screenplay.Question;
import framework.screenplay.abilities.AwaitPatiently;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.apache.commons.lang3.function.FailableSupplier;
import org.apache.commons.lang3.stream.Streams;
import org.assertj.core.api.Assert;
import org.assertj.core.api.HamcrestCondition;
import org.hamcrest.Matcher;

public class See {

  /**
   * then(actor).should(See.that(TheRemembered.valueOf("customerId", String.class),
   * Matchers.is("Tequila123")))
   */
  public static <T> Consequence that(Question<T> question, Matcher<? super T> matcher) {
    return actor -> actor.should(See.that(question.answeredBy(actor), matcher));
  }

  public static <T> Consequence that(Question<T> question, Predicate<? super T> predicate) {
    return actor ->
        actor.should(See.that(predicate.test(question.answeredBy(actor)))).isEqualTo(Boolean.TRUE);
  }

  @SafeVarargs
  public static <T> Consequence that(T actual, Matcher<? super T>... matchers) {
    return actor ->
        Streams.failableStream(Arrays.stream(matchers))
            .forEach(
                matcher -> actor.should(See.that(actual)).is(HamcrestCondition.matching(matcher)));
  }

  // converts value to question
  public static <T> Question<T> that(T actual) {
    return actor -> actual;
  }

  public static <T> Question<T> that(Question<T> question) {
    return question;
  }

  public static <T> Consequence eventually(Question<T> question, Matcher<? super T> matcher) {
    return actor ->
        actor
            .usingAbilityTo(AwaitPatiently.class)
            .conditionFactory()
            .untilAsserted(
                () ->
                    actor
                        .should(See.that(question.answeredBy(actor)))
                        .is(HamcrestCondition.matching(matcher)));
  }

  public static <T> Consequence eventually(
      Question<T> question, Consumer<? super T> assertConsumer) {
    return actor ->
        actor
            .usingAbilityTo(AwaitPatiently.class)
            .conditionFactory()
            .untilAsserted(() -> actor.asksFor(question), assertConsumer);
  }

  public static Consequence eventually(FailableSupplier<Assert<?, ?>, Throwable> assertSupplier) {
    return actor ->
        actor
            .usingAbilityTo(AwaitPatiently.class)
            .conditionFactory()
            .untilAsserted(assertSupplier::get);
  }
}
