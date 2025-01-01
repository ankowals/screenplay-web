package framework.screenplay;

import framework.screenplay.actor.Actor;

@FunctionalInterface
public interface Consequence {
  void evaluateFor(Actor actor);
}
