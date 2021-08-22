package screenplay.framework;

import screenplay.framework.actor.Actor;

@FunctionalInterface
public interface Integration<T> {
    T integratedBy(Actor actor);
}
