package framework.screenplay.helpers;

import framework.screenplay.actor.Actor;

public class Bdd {
  public static <T> T given(T t) {
    return t;
  }

  public static <T extends Actor> T when(T actor) {
    return actor;
  }

  public static <T extends Actor> T then(T actor) {
    return actor;
  }

  public static <T> T and(T t) {
    return t;
  }

  public static <T> T but(T t) {
    return t;
  }
}
