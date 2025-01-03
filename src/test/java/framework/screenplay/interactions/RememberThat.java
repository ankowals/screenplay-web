package framework.screenplay.interactions;

import framework.screenplay.Interaction;
import framework.screenplay.Question;

public class RememberThat {

  public static <T> RememberThatFactory<T> valueOf(String name) {
    return new RememberThatFactory<>(name);
  }

  public static class RememberThatFactory<T> {

    private final String name;

    RememberThatFactory(String name) {
      this.name = name;
    }

    public Interaction is(T value) {
      return actor -> actor.remembers(this.name, value);
    }

    public Interaction is(Question<?> question) {
      return actor -> actor.remembers(this.name, question);
    }
  }
}
