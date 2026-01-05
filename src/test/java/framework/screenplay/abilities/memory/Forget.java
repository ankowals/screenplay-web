package framework.screenplay.abilities.memory;

import framework.screenplay.Interaction;
import framework.screenplay.actor.use.UseAbility;

public class Forget {
  public static Interaction valueOf(String name, Class<?> type) {
    return actor -> UseAbility.of(actor).to(RememberThings.class).memory().forget(name, type);
  }

  public static Interaction everything() {
    return actor -> UseAbility.of(actor).to(RememberThings.class).memory().clear();
  }
}
