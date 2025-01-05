package framework.screenplay.memory;

import framework.screenplay.Question;
import framework.screenplay.actor.UseAbility;

public class Remembered {
  public static <T> Question<T> valueOf(String name) {
    return actor -> UseAbility.of(actor).to(RememberThings.class).memory().recall(name);
  }
}
