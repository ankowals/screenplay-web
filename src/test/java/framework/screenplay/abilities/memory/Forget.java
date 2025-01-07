package framework.screenplay.abilities.memory;

import framework.screenplay.Interaction;
import framework.screenplay.abilities.use.UseAbility;
import framework.screenplay.actor.Actor;

public class Forget {
  public static Interaction<Actor> valueOf(String name) {
    return actor -> UseAbility.of(actor).to(RememberThings.class).memory().forget(name);
  }

  public static Interaction<Actor> everything() {
    return actor -> UseAbility.of(actor).to(RememberThings.class).memory().clear();
  }
}
