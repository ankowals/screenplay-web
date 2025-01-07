package framework.screenplay.abilities.memory;

import framework.screenplay.Interaction;
import framework.screenplay.Question;
import framework.screenplay.abilities.UseAbility;
import framework.screenplay.actor.Actor;

public class RememberThat {

  public static <T> RememberThatFactory<T> valueOf(String name) {
    return new RememberThatFactory<>(name);
  }

  public static class RememberThatFactory<T> {

    private final String name;

    RememberThatFactory(String name) {
      this.name = name;
    }

    public Interaction<Actor> is(T value) {
      return actor ->
          UseAbility.of(actor).to(RememberThings.class).memory().remember(this.name, value);
    }

    public Interaction<Actor> is(Question<?, Actor> question) {
      return actor ->
          UseAbility.of(actor)
              .to(RememberThings.class)
              .memory()
              .remember(this.name, question.answeredBy(actor));
    }
  }
}
