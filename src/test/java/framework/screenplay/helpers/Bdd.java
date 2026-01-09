package framework.screenplay.helpers;

import framework.screenplay.Ability;
import framework.screenplay.Consequence;
import framework.screenplay.Interaction;
import framework.screenplay.Question;
import framework.screenplay.actor.Actor;
import framework.screenplay.actor.ManagesAbilities;
import framework.screenplay.actor.PerformsChecks;
import framework.screenplay.actor.PerformsInteractions;

public class Bdd {
  public static BddActorWrapper given(Actor actor) {
    return new BddActorWrapper(actor);
  }

  public static Actor when(Actor actor) {
    return actor;
  }

  public static Actor then(Actor actor) {
    return actor;
  }

  public static BddActorWrapper and(Actor actor) {
    return new BddActorWrapper(actor);
  }

  public static BddActorWrapper but(Actor actor) {
    return new BddActorWrapper(actor);
  }

  public static class BddActorWrapper
      implements PerformsInteractions, PerformsChecks, ManagesAbilities {

    private final Actor actor;

    BddActorWrapper(Actor actor) {
      this.actor = actor;
    }

    @Override
    @SafeVarargs
    public final <T extends Ability> void can(T... doSomething) {
      this.actor.can(doSomething);
    }

    @Override
    public <T extends Ability> T usingAbilityTo(Class<T> doSomething) {
      return this.actor.usingAbilityTo(doSomething);
    }

    @Override
    public final void attemptsTo(Interaction... interactions) {
      this.actor.attemptsTo(interactions);
    }

    @Override
    public <T> T asksFor(Question<T> question) throws Exception {
      return this.actor.asksFor(question);
    }

    @Override
    public void should(Consequence... consequences) throws Exception {
      this.actor.should(consequences);
    }

    public final void wasAbleTo(Interaction... interactions) {
      this.actor.attemptsTo(interactions);
    }

    public final void has(Interaction... interactions) {
      this.actor.attemptsTo(interactions);
    }

    public final void was(Interaction... interactions) {
      this.actor.attemptsTo(interactions);
    }

    public final void is(Interaction... interactions) {
      this.actor.attemptsTo(interactions);
    }
  }
}
