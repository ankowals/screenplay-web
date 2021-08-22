package screenplay.framework;

import screenplay.framework.actor.Actor;

@FunctionalInterface
public interface Interaction {
    void performAs(Actor actor) throws Exception;
}
