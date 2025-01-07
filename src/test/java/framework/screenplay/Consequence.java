package framework.screenplay;

import framework.screenplay.actor.Actor;

@FunctionalInterface
public interface Consequence<T extends Actor> {
  void evaluateFor(T actor) throws Exception;
}
