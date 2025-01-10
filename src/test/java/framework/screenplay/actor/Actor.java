package framework.screenplay.actor;

import framework.screenplay.*;
import java.util.*;
import org.apache.commons.lang3.function.Failable;
import org.assertj.core.api.*;
import org.hamcrest.Matcher;

public class Actor implements PerformsInteractions, PerformsChecks, ManagesAbilities {

  private final Map<Class<?>, Ability> abilities = new HashMap<>();

  @SafeVarargs
  @Override
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
  public void should(Consequence consequence) throws Exception {
    consequence.evaluateFor(this);
  }

  @Override
  public <T> ObjectAssert<T> assertsThat(T actual) {
    return Assertions.assertThat(actual);
  }

  public final void wasAbleTo(Interaction... interactions) {
    this.attemptsTo(interactions);
  }

  public final void has(Interaction... interactions) {
    this.attemptsTo(interactions);
  }

  public final void was(Interaction... interactions) {
    this.attemptsTo(interactions);
  }

  public final void is(Interaction... interactions) {
    this.attemptsTo(interactions);
  }

  @SafeVarargs
  public final <T> void checksThat(T actual, Matcher<? super T>... matchers) {
    Arrays.asList(matchers)
        .forEach(matcher -> this.assertsThat(actual).is(HamcrestCondition.matching(matcher)));
  }

  public <T> ObjectAssert<T> should(Question<T> question) throws Exception {
    return this.should(question.answeredBy(this));
  }

  public <T> ObjectAssert<T> should(T actual) {
    return this.assertsThat(actual);
  }

  public <T> ObjectAssert<T> assertsThat(Question<T> question) throws Exception {
    return this.assertsThat(question.answeredBy(this));
  }
}
