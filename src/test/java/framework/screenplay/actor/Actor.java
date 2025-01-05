package framework.screenplay.actor;

import static org.assertj.core.api.HamcrestCondition.matching;

import framework.screenplay.*;
import framework.screenplay.assertions.AssertSoftly;
import framework.screenplay.helpers.See;
import java.util.*;
import org.apache.commons.lang3.function.Failable;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matcher;

public class Actor implements PerformsInteractions, PerformsChecks, ManagesAbilities {

  @SuppressWarnings("rawtypes")
  protected final Map<Class, Ability> abilities = new HashMap<>();

  @Override
  public <T extends Ability> void can(T... doSomething) {
    Arrays.stream(doSomething).forEach(ability -> this.abilities.put(ability.getClass(), ability));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends Ability> T usingAbilityTo(Class<? extends T> doSomething) {
    if (this.abilities.get(doSomething) == null) throw new NoMatchingAbilityException(doSomething);

    return (T) this.abilities.get(doSomething);
  }

  @Override
  public void attemptsTo(Interaction... interactions) {
    Failable.stream(Arrays.asList(interactions))
        .forEach(interaction -> interaction.performAs(this));
  }

  public void wasAbleTo(Interaction... interactions) {
    this.attemptsTo(interactions);
  }

  public void has(Interaction... interactions) {
    this.attemptsTo(interactions);
  }

  public void was(Interaction... interactions) {
    this.attemptsTo(interactions);
  }

  public void is(Interaction... interactions) {
    this.attemptsTo(interactions);
  }

  @Override
  public <T> T asksFor(Question<T> question) {
    return question.answeredBy(this);
  }

  @SafeVarargs
  @Override
  public final <T> void checksThat(T actual, Matcher<? super T>... matchers) {
    Arrays.asList(matchers).forEach(matcher -> this.should(See.that(actual)).is(matching(matcher)));
  }

  @SafeVarargs
  public final <T> void expects(Question<T> question, Matcher<? super T>... matchers) {
    this.checksThat(question.answeredBy(this), matchers);
  }

  @SafeVarargs
  public final <T> void should(T actual, Matcher<? super T>... matchers) {
    this.checksThat(actual, matchers);
  }

  public void should(Consequence consequence) {
    consequence.evaluateFor(this);
  }

  public final void can(Consequence consequence) {
    this.should(consequence);
  }

  public <T, E extends AbstractObjectAssert<E, T>> E should(Question<T> question) {
    return this.should(question.answeredBy(this));
  }

  public <T, E extends AbstractObjectAssert<E, T>> E should(T actual) {
    return this.assertsThat(actual);
  }

  public <T, E extends AbstractObjectAssert<E, T>> E assertsThat(Question<T> question) {
    return this.assertsThat(question.answeredBy(this));
  }

  @SuppressWarnings("unchecked")
  public <T, E extends AbstractObjectAssert<E, T>> E assertsThat(T actual) {
    if (this.abilities.get(AssertSoftly.class) != null) {
      return (E) this.usingAbilityTo(AssertSoftly.class).softAssertions().assertThat(actual);
    }

    return (E) Assertions.assertThat(actual);
  }
}
