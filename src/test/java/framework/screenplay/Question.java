package framework.screenplay;

import framework.screenplay.actor.Actor;

@FunctionalInterface
public interface Question<T> {
    T answeredBy(Actor actor);
}
