package framework.screenplay.helpers;

import framework.screenplay.Consequence;
import framework.screenplay.Question;
import org.hamcrest.Matcher;

public class See {

  public static <T> Consequence that(Question<T> question, Matcher<? super T> matcher) {
    return new <T>QuestionConsequence<T>(question, matcher);
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
