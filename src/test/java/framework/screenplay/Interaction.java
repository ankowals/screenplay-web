package framework.screenplay;

import framework.screenplay.actor.Actor;

@FunctionalInterface
public interface Interaction<T extends Actor> {
  void performAs(T actor) throws Exception;
}
