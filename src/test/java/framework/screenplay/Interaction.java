package framework.screenplay;

import framework.screenplay.actor.Actor;

@FunctionalInterface
public interface Interaction {
  void performAs(Actor actor) throws Exception;
}
