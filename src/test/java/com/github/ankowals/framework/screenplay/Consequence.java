package com.github.ankowals.framework.screenplay;

import com.github.ankowals.framework.screenplay.actor.Actor;

@FunctionalInterface
public interface Consequence {
  void evaluateFor(Actor actor) throws Exception;
}
