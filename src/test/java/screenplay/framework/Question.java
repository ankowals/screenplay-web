package screenplay.framework;

import screenplay.framework.actor.Actor;

@FunctionalInterface
public interface Question<T> {
    T answeredBy(Actor actor);
}
