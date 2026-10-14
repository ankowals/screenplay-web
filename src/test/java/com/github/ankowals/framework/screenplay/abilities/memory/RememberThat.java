package com.github.ankowals.framework.screenplay.abilities.memory;

import com.github.ankowals.framework.screenplay.Interaction;
import com.github.ankowals.framework.screenplay.Question;

public class RememberThat {

  public static <T> RememberThatFactory<T> valueOf(Memory.Key<T> key) {
    return RememberThat.valueOf(key.name());
  }

  public static <T> RememberThatFactory<T> valueOf(String name) {
    return new RememberThatFactory<>(name);
  }

  public static class RememberThatFactory<T> {

    private final String name;

    RememberThatFactory(String name) {
      this.name = name;
    }

    public Interaction is(T value) {
      return actor -> RememberThings.as(actor).remember(this.name, value);
    }

    public Interaction is(Question<?> question) {
      return actor -> RememberThings.as(actor).remember(this.name, question.answeredBy(actor));
    }
  }
}
