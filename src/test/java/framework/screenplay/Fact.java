package framework.screenplay;

import framework.screenplay.actor.Actor;

@FunctionalInterface
public interface Fact {
    void setupFor(Actor actor) throws Exception;
}
