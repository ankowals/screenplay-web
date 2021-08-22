package screenplay.framework.actor;

import screenplay.framework.Ability;

public interface ManagesAbilities {
    <T extends Ability> ManagesAbilities can(T doSomething);
    <T extends Ability> T using(Class<? extends T> ability);
}
