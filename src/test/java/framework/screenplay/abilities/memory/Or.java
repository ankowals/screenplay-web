package framework.screenplay.abilities.memory;

import framework.screenplay.Question;

public class Or {
  public static <T> Question<T> empty() {
    return actor -> null;
  }

  public static <T> Question<T> askFor(Question<T> question) {
    return question;
  }
}
