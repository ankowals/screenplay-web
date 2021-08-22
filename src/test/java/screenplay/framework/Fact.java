package screenplay.framework;

import screenplay.framework.actor.Actor;

@FunctionalInterface
public interface Fact {
    void setupFor(Actor actor) throws Exception;
}
