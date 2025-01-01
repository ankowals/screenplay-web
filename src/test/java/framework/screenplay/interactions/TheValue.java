package framework.screenplay.interactions;

import framework.screenplay.Question;

public class TheValue {
  public static <T> Question<T> of(String name) {
    return actor -> actor.recall(name);
  }
}
