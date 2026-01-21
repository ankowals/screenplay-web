package com.github.ankowals.framework.screenplay.helpers.bdd;

import com.github.ankowals.framework.screenplay.actor.Actor;

public class Bdd {
  public static BddActorWrapper given(Actor actor) {
    return new BddActorWrapper(actor);
  }

  public static Actor when(Actor actor) {
    return actor;
  }

  public static Actor then(Actor actor) {
    return actor;
  }

  public static BddActorWrapper and(Actor actor) {
    return new BddActorWrapper(actor);
  }

  public static BddActorWrapper but(Actor actor) {
    return new BddActorWrapper(actor);
  }
}
