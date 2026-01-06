package framework.screenplay.abilities.memory;

import framework.screenplay.Question;
import framework.screenplay.helpers.use.UseAbility;

public class TheRemembered {
  public static <T> Question<T> valueOf(String key, Class<T> type) {
    return actor -> {
      T value = UseAbility.of(actor).to(RememberThings.class).memory().recall(key, type);

      if (value == null) {
        throw new NoObjectToRecallException(key);
      }

      return value;
    };
  }
}
