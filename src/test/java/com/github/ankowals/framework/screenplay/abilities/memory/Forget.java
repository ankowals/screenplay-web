package com.github.ankowals.framework.screenplay.abilities.memory;

import com.github.ankowals.framework.screenplay.Interaction;

public class Forget {
  public static Interaction valueOf(Memory.Key<?> key) {
    return Forget.valueOf(key.name(), key.type());
  }

  public static Interaction valueOf(String name, Class<?> type) {
    return actor -> RememberThings.as(actor).forget(name, type);
  }

  public static Interaction everything() {
    return actor -> RememberThings.as(actor).forget();
  }
}
