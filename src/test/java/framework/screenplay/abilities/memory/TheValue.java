package framework.screenplay.abilities.memory;

import framework.screenplay.Question;
import framework.screenplay.abilities.use.UseAbility;

public class TheValue {
  public static <T> Question<T> rememberedAs(String name, Class<T> type) {
    return actor -> UseAbility.of(actor).to(RememberThings.class).memory().recall(name, type);
  }
}
