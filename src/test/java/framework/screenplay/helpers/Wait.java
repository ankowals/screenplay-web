package framework.screenplay.helpers;

import framework.screenplay.Interaction;
import framework.screenplay.Question;
import framework.screenplay.abilities.AwaitPatiently;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import org.hamcrest.Matcher;

public class Wait {
  public static Interaction eventually(Callable<Boolean> expectedState) {
    return actor ->
        actor.usingAbilityTo(AwaitPatiently.class).conditionFactory().until(expectedState);
  }

  public static <T> Question<T> eventually(Question<T> question, Matcher<? super T> matcher) {
    return actor ->
        actor
            .usingAbilityTo(AwaitPatiently.class)
            .conditionFactory()
            .until(() -> question.answeredBy(actor), matcher);
  }

  public static <T> Question<T> eventually(Question<T> question, Predicate<? super T> predicate) {
    return actor ->
        actor
            .usingAbilityTo(AwaitPatiently.class)
            .conditionFactory()
            .until(() -> question.answeredBy(actor), predicate);
  }
}
