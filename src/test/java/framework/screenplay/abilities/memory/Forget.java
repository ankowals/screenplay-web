package framework.screenplay.abilities.memory;

import framework.screenplay.Interaction;
import framework.screenplay.helpers.use.UseAbility;

public class Forget {
  public static Interaction valueOf(Memory.Key<?> key) {
    return Forget.valueOf(key.name(), key.type());
  }

  public static Interaction valueOf(String name, Class<?> type) {
    return actor -> UseAbility.of(actor).to(RememberThings.class).memory().forget(name, type);
  }

  public static Interaction everything() {
    return actor -> UseAbility.of(actor).to(RememberThings.class).memory().clear();
  }
}
