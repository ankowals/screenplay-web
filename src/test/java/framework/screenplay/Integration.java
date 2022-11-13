package framework.screenplay;

import framework.screenplay.actor.Actor;

@FunctionalInterface
public interface Integration<T> {
    T integratedBy(Actor actor);
}
