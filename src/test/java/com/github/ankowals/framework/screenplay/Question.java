package com.github.ankowals.framework.screenplay;

import com.github.ankowals.framework.screenplay.actor.Actor;

@FunctionalInterface
public interface Question<T> {
  T answeredBy(Actor actor) throws Exception;
}
