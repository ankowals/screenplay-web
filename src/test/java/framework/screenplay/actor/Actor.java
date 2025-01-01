package framework.screenplay.actor;

import static org.assertj.core.api.HamcrestCondition.matching;

import framework.screenplay.*;
import framework.screenplay.exceptions.NoMatchingAbilityException;
import framework.screenplay.exceptions.NoObjectToRecallException;
import framework.screenplay.helpers.See;
import java.util.*;
import org.apache.commons.lang3.function.Failable;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matcher;

public class Actor
    implements PerformsInteractions,
        PerformsChecks,
        ManagesAbilities,
        AsksQuestions,
        RemembersThings {

  private SoftAssertions softAssertions;

  @SuppressWarnings("rawtypes")
  protected final Map<Class, Ability> abilities = new HashMap<>();

  protected final Map<String, Object> memory = new HashMap<>();

  public Actor() {}

  public Actor(SoftAssertions softAssertions) {
    this.softAssertions = softAssertions;
  }

  @Override
  public <T extends Ability> Actor can(T doSomething) {
    this.abilities.put(doSomething.getClass(), doSomething);
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends Ability> T usingAbilityTo(Class<? extends T> doSomething) {
    if (this.abilities.get(doSomething) == null) throw new NoMatchingAbilityException(doSomething);

    return (T) this.abilities.get(doSomething);
  }

  @Override
  public PerformsInteractions attemptsTo(Interaction... interactions) {
    Failable.stream(Arrays.asList(interactions))
        .forEach(interaction -> interaction.performAs(this));
    return this;
  }

  @Override
  public PerformsInteractions wasAbleTo(Interaction... interactions) {
    return this.attemptsTo(interactions);
  }

  @Override
  public PerformsInteractions has(Interaction... interactions) {
    return this.attemptsTo(interactions);
  }

  @Override
  public PerformsInteractions was(Interaction... interactions) {
    return this.attemptsTo(interactions);
  }

  @Override
  public PerformsInteractions is(Interaction... interactions) {
    return this.attemptsTo(interactions);
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
  @Override
  public final <T> void expects(Question<T> question, Matcher<? super T>... matchers) {
    this.checksThat(question.answeredBy(this), matchers);
  }

  @SafeVarargs
  @Override
  public final <T> PerformsChecks should(T actual, Matcher<? super T>... matchers) {
    this.checksThat(actual, matchers);
    return this;
  }

  @Override
  public PerformsChecks should(Consequence consequence) {
    consequence.evaluateFor(this);
    return this;
  }

  @Override
  public <T, E extends AbstractObjectAssert<E, T>> E should(Question<T> question) {
    return this.should(question.answeredBy(this));
  }

  @Override
  public <T, E extends AbstractObjectAssert<E, T>> E should(T actual) {
    return this.assertsThat(actual);
  }

  @Override
  public <T, E extends AbstractObjectAssert<E, T>> E assertsThat(Question<T> question) {
    return this.assertsThat(question.answeredBy(this));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T, E extends AbstractObjectAssert<E, T>> E assertsThat(T actual) {
    if (this.softAssertions != null) return (E) this.softAssertions.assertThat(actual);

    return (E) Assertions.assertThat(actual);
  }

  @Override
  public <T> void remembers(String name, Question<T> question) {
    this.remembers(name, this.asksFor(question));
  }

  @Override
  public <T> void remembers(String name, T value) {
    this.memory.put(name, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T> T recall(String name) {
    if (this.memory.get(name) == null) throw new NoObjectToRecallException(name);

    return (T) this.memory.get(name);
  }
}
