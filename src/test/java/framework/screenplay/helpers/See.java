package framework.screenplay.helpers;

import framework.screenplay.Consequence;
import framework.screenplay.Question;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.apache.commons.lang3.function.FailableSupplier;
import org.assertj.core.api.Assert;
import org.assertj.core.api.HamcrestCondition;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.hamcrest.Matcher;

public class See {

  private static final Supplier<ConditionFactory> DEFAULT_CONDITION_FACTORY =
      () -> Awaitility.await().ignoreExceptions();

  /**
   * then(actor).should(See.that(TheRemembered.valueOf("customerId", String.class),
   * Matchers.is("Tequila123")))
   */
  public static <T> Consequence that(Question<T> question, Matcher<? super T> matcher) {
    return actor -> actor.checksThat(question.answeredBy(actor), matcher);
  }

  public static Consequence that(Consequence consequence) {
    return consequence;
  }

  public static <T> T that(T actual) {
    return actual;
  }

  public static <T> Question<T> that(Question<T> question) {
    return question;
  }

  public static <T> Consequence eventually(Question<T> question, Matcher<? super T> matcher) {
    return See.eventually(question, matcher, DEFAULT_CONDITION_FACTORY);
  }

  public static <T> Consequence eventually(
      Question<T> question, Matcher<? super T> matcher, Supplier<ConditionFactory> customizer) {
    return actor ->
        customizer
            .get()
            .untilAsserted(
                () ->
                    actor
                        .assertsThat(question.answeredBy(actor))
                        .is(HamcrestCondition.matching(matcher)));
  }

  public static <T> Consequence eventually(
      Question<T> question, Consumer<? super T> assertConsumer) {
    return See.eventually(question, assertConsumer, DEFAULT_CONDITION_FACTORY);
  }

  public static <T> Consequence eventually(
      Question<T> question,
      Consumer<? super T> assertConsumer,
      Supplier<ConditionFactory> customizer) {
    return actor -> customizer.get().untilAsserted(() -> actor.asksFor(question), assertConsumer);
  }

  public static Consequence eventually(
      FailableSupplier<Assert<?, ?>, Throwable> assertSupplier) {
    return See.eventually(assertSupplier, DEFAULT_CONDITION_FACTORY);
  }

  public static Consequence eventually(
      FailableSupplier<Assert<?, ?>, Throwable> assertSupplier,
      Supplier<ConditionFactory> customizer) {
    return actor -> customizer.get().untilAsserted(assertSupplier::get);
  }
}
