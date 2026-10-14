package com.github.ankowals.framework.screenplay.helpers.bdd;

import com.github.ankowals.framework.screenplay.Ability;
import com.github.ankowals.framework.screenplay.Consequence;
import com.github.ankowals.framework.screenplay.Interaction;
import com.github.ankowals.framework.screenplay.Question;
import com.github.ankowals.framework.screenplay.actor.Actor;

public class BddActorWrapper {

  private final Actor actor;

  BddActorWrapper(Actor actor) {
    this.actor = actor;
  }

  public final void can(Ability... doSomething) {
    this.actor.can(doSomething);
  }

  public <T extends Ability> T usingAbilityTo(Class<T> doSomething) {
    return this.actor.usingAbilityTo(doSomething);
  }

  public final void attemptsTo(Interaction... interactions) {
    this.actor.attemptsTo(interactions);
  }

  public <T> T asksFor(Question<T> question) throws Exception {
    return this.actor.asksFor(question);
  }

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
