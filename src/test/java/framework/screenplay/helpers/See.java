package framework.screenplay.helpers;

import framework.screenplay.Consequence;
import framework.screenplay.Question;
import framework.screenplay.abilities.AwaitPatiently;
import framework.screenplay.helpers.use.UseAbility;
import java.util.Arrays;
import java.util.function.Consumer;
import org.apache.commons.lang3.function.FailableConsumer;
import org.apache.commons.lang3.function.FailableSupplier;
import org.apache.commons.lang3.stream.Streams;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.HamcrestCondition;
import org.hamcrest.Matcher;

/**
 * then(actor).should(See.that(TheRemembered.valueOf("customerId", String.class),
 * Matchers.is("Tequila123")))
 */
public class See {

  public static <T> Consequence that(T actual, Matcher<? super T> matcher) {
    return actor -> {
      Question<T> question = a -> actual;
      actor.should(See.that(question, matcher));
    };
  }

  @SafeVarargs
  public static <T> Consequence that(Question<T> question, Matcher<? super T>... matchers) {
    return actor -> {
      T answer = actor.asksFor(question);
      Streams.failableStream(Arrays.stream(matchers))
          .forEach(
              matcher -> Assertions.assertThat(answer).is(HamcrestCondition.matching(matcher)));
    };
  }

  public static <T> Consequence that(
      Question<T> question, FailableConsumer<? super T, Exception> assertConsumer) {
    return actor -> assertConsumer.accept(actor.asksFor(question));
  }

  public static <T> Consequence eventually(Question<T> question, Matcher<? super T> matcher) {
    return actor ->
        UseAbility.of(actor)
            .to(AwaitPatiently.class)
            .conditionFactory()
            .untilAsserted(
                () ->
                    actor.should(
                        See.that(
                            question,
                            answer ->
                                Assertions.assertThat(answer)
                                    .is(HamcrestCondition.matching(matcher)))));
  }

  public static <T> Consequence eventually(
      Question<T> question, Consumer<? super T> assertConsumer) {
    return actor ->
        UseAbility.of(actor)
            .to(AwaitPatiently.class)
            .conditionFactory()
            .untilAsserted(() -> actor.asksFor(question), assertConsumer);
  }

  public static <T> Consequence eventually(
      FailableSupplier<Assert<?, T>, Throwable> assertSupplier) {
    return actor ->
        UseAbility.of(actor)
            .to(AwaitPatiently.class)
            .conditionFactory()
            .untilAsserted(assertSupplier::get);
  }
}
