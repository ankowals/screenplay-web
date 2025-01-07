package framework.screenplay.helpers;

import framework.screenplay.Consequence;
import framework.screenplay.Question;
import framework.screenplay.actor.Actor;
import org.hamcrest.Matcher;

public class See {

  public static <T, U extends Actor> Consequence<U> that(
      Question<T, U> question, Matcher<? super T> matcher) {
    return actor -> actor.checksThat(question.answeredBy(actor), matcher);
  }

  public static <T extends Actor> Consequence<T> that(Consequence<T> consequence) {
    return consequence;
  }

  public static <T> T that(T actual) {
    return actual;
  }

  public static <T, U extends Actor> Question<T, U> that(Question<T, U> question) {
    return question;
  }
}
