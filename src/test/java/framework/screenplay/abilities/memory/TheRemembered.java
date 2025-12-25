package framework.screenplay.abilities.memory;

import framework.screenplay.Question;
import framework.screenplay.abilities.use.UseAbility;

public class TheRemembered {
  public static <T> Question<T> valueOf(String key, Class<T> type) {
    return actor -> UseAbility.of(actor).to(RememberThings.class).memory().recall(key, type);
  }
}
