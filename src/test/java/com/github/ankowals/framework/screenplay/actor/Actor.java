package com.github.ankowals.framework.screenplay.actor;

import com.github.ankowals.framework.screenplay.*;
import java.util.*;
import org.apache.commons.lang3.function.Failable;

public class Actor {

  private final Map<Class<?>, Ability> abilities = new HashMap<>();

  public final void can(Ability... doSomething) {
    Arrays.stream(doSomething).forEach(ability -> this.abilities.put(ability.getClass(), ability));
  }

  public <T extends Ability> T usingAbilityTo(Class<T> doSomething) {
    return doSomething.cast(this.abilities.get(doSomething));
  }

  public final void attemptsTo(Interaction... interactions) {
    Failable.stream(Arrays.asList(interactions))
        .forEach(interaction -> interaction.performAs(this));
  }

  public <T> T asksFor(Question<T> question) throws Exception {
    return question.answeredBy(this);
  }

  public void should(Consequence... consequences) {
    Failable.stream(Arrays.asList(consequences))
        .forEach(consequence -> consequence.evaluateFor(this));
  }
}
