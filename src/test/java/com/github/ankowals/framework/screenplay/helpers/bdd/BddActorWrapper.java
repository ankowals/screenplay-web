package com.github.ankowals.framework.screenplay.helpers.bdd;

import com.github.ankowals.framework.screenplay.Ability;
import com.github.ankowals.framework.screenplay.Consequence;
import com.github.ankowals.framework.screenplay.Interaction;
import com.github.ankowals.framework.screenplay.Question;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.actor.ManagesAbilities;
import com.github.ankowals.framework.screenplay.actor.PerformsChecks;
import com.github.ankowals.framework.screenplay.actor.PerformsInteractions;

public class BddActorWrapper implements PerformsInteractions, PerformsChecks, ManagesAbilities {

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
