package framework.screenplay.abilities.memory;

import framework.screenplay.Question;
import framework.screenplay.abilities.UseAbility;
import framework.screenplay.actor.Actor;

public class Remembered {
  public static <T> Question<T, Actor> valueOf(String name) {
    return actor -> UseAbility.of(actor).to(RememberThings.class).memory().recall(name);
  }
}
