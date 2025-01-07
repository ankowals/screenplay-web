package framework.screenplay;

import framework.screenplay.actor.Actor;

@FunctionalInterface
public interface Question<T, U extends Actor> {
  T answeredBy(U actor) throws Exception;
}
