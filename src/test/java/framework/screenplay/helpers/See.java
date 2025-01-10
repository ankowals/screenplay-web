package framework.screenplay.helpers;

import framework.screenplay.Consequence;
import framework.screenplay.Question;
import org.hamcrest.Matcher;

public class See {

  /**
   * then(actor).should(See.that(Remembered.valueOf("customerId", String.class), is("Tequila123")))
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
}
