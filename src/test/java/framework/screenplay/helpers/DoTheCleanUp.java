package framework.screenplay.helpers;

import framework.screenplay.hooks.OnTeardown;
import framework.screenplay.Ability;
import framework.screenplay.actor.Actor;

public record DoTheCleanUp(OnTeardown onTeardown) implements Ability {
    public static OnTeardown as(Actor actor) { return actor.usingAbilityTo(DoTheCleanUp.class).onTeardown(); }
    public static DoTheCleanUp with(OnTeardown onTeardown) {
        return new DoTheCleanUp(onTeardown);
    }
}
