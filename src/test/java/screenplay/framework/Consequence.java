package screenplay.framework;

import screenplay.framework.actor.Actor;

@FunctionalInterface
public interface Consequence {
    void evaluateFor(Actor actor);
}
