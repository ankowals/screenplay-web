package com.github.ankowals.framework.screenplay.helpers;

import com.github.ankowals.framework.screenplay.Interaction;
import com.github.ankowals.framework.screenplay.Question;
import com.github.ankowals.framework.screenplay.abilities.AwaitPatiently;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import org.hamcrest.Matcher;

public class Wait {
  public static Interaction until(Callable<Boolean> expectedState) {
    return actor -> AwaitPatiently.as(actor).until(expectedState);
  }

  public static <T> Question<T> until(Question<T> question, Matcher<? super T> matcher) {
    return actor -> AwaitPatiently.as(actor).until(() -> question.answeredBy(actor), matcher);
  }

  public static <T> Question<T> until(Question<T> question, Predicate<? super T> predicate) {
    return actor -> AwaitPatiently.as(actor).until(() -> question.answeredBy(actor), predicate);
  }
}
