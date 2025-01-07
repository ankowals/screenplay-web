package framework.screenplay.actor;

import framework.screenplay.*;
import java.util.*;
import org.apache.commons.lang3.function.Failable;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.HamcrestCondition;
import org.hamcrest.Matcher;

public class Actor implements PerformsInteractions, PerformsChecks, ManagesAbilities {

  @SuppressWarnings("rawtypes")
  protected final Map<Class, Ability> abilities = new HashMap<>();

  @SafeVarargs
  @Override
  public final <T extends Ability> void can(T... doSomething) {
    Arrays.stream(doSomething).forEach(ability -> this.abilities.put(ability.getClass(), ability));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends Ability> T usingAbilityTo(Class<? extends T> doSomething) {
    return (T) this.abilities.get(doSomething);
  }

  @SafeVarargs
  @Override
  public final void attemptsTo(Interaction<Actor>... interactions) {
    Failable.stream(Arrays.asList(interactions))
        .forEach(interaction -> interaction.performAs(this));
  }

  @Override
  public <T> T asksFor(Question<T, Actor> question) throws Exception {
    return question.answeredBy(this);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T, E extends AbstractObjectAssert<E, T>> E assertsThat(T actual) {
    return (E) Assertions.assertThat(actual);
  }

  @Override
  public void should(Consequence<Actor> consequence) throws Exception {
    consequence.evaluateFor(this);
  }

  @SafeVarargs
  public final void wasAbleTo(Interaction<Actor>... interactions) {
    this.attemptsTo(interactions);
  }

  @SafeVarargs
  public final void has(Interaction<Actor>... interactions) {
    this.attemptsTo(interactions);
  }

  @SafeVarargs
  public final void was(Interaction<Actor>... interactions) {
    this.attemptsTo(interactions);
  }

  @SafeVarargs
  public final void is(Interaction<Actor>... interactions) {
    this.attemptsTo(interactions);
  }

  @SafeVarargs
  public final <T> void checksThat(T actual, Matcher<? super T>... matchers) {
    Arrays.asList(matchers)
        .forEach(matcher -> this.assertsThat(actual).is(HamcrestCondition.matching(matcher)));
  }

  @SafeVarargs
  public final <T> void checksThat(Question<T, Actor> question, Matcher<? super T>... matchers)
      throws Exception {
    this.checksThat(question.answeredBy(this), matchers);
  }

  public <T, E extends AbstractObjectAssert<E, T>> E should(Question<T, Actor> question)
      throws Exception {
    return this.should(question.answeredBy(this));
  }

  public <T, E extends AbstractObjectAssert<E, T>> E should(T actual) {
    return this.assertsThat(actual);
  }

  public <T, E extends AbstractObjectAssert<E, T>> E assertsThat(Question<T, Actor> question)
      throws Exception {
    return this.assertsThat(question.answeredBy(this));
  }
}
