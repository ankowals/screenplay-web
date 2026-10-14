package com.github.ankowals.framework.screenplay.helpers;

import com.github.ankowals.framework.screenplay.Question;

public class OrElse {
  public static <T> Question<T> empty() {
    return actor -> null;
  }

  public static <T> Question<T> value(T value) {
    return actor -> value;
  }

  public static <T> Question<T> askFor(Question<T> question) {
    return question;
  }
}
