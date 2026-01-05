package framework.screenplay.actor;

import framework.screenplay.*;
import java.util.*;
import org.apache.commons.lang3.function.Failable;
import org.assertj.core.api.*;

public class Actor implements PerformsInteractions, PerformsChecks, ManagesAbilities {

  private final Map<Class<?>, Ability> abilities = new HashMap<>();

  @Override
  @SafeVarargs
  public final <T extends Ability> void can(T... doSomething) {
    Arrays.stream(doSomething).forEach(ability -> this.abilities.put(ability.getClass(), ability));
  }

  @Override
  public <T extends Ability> T usingAbilityTo(Class<T> doSomething) {
    return doSomething.cast(this.abilities.get(doSomething));
  }

  @Override
  public final void attemptsTo(Interaction... interactions) {
    Failable.stream(Arrays.asList(interactions))
        .forEach(interaction -> interaction.performAs(this));
  }

  @Override
  public <T> T asksFor(Question<T> question) throws Exception {
    return question.answeredBy(this);
  }

  @Override
  public void should(Consequence... consequences) throws Exception {
    Failable.stream(Arrays.asList(consequences))
        .forEach(consequence -> consequence.evaluateFor(this));
  }

  @Override
  public <T> ObjectAssert<T> should(Question<T> question) throws Exception {
    return Assertions.assertThat(this.asksFor(question));
  }
}
