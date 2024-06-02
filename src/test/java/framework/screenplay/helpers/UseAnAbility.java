package framework.screenplay.helpers;

import framework.screenplay.Ability;
import framework.screenplay.actor.Actor;

public class UseAnAbility {

    private final Actor actor;

    public UseAnAbility(Actor actor) {
        this.actor = actor;
    }

    public static UseAnAbility of(Actor actor) {
        return new UseAnAbility(actor);
    }

    public <T extends Ability> T to(Class<T> doSomething) {
        return this.actor.usingAbilityTo(doSomething);
    }
}
