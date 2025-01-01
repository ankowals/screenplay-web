package framework.screenplay.actor;

import framework.screenplay.Consequence;
import framework.screenplay.Question;
import org.assertj.core.api.AbstractObjectAssert;
import org.hamcrest.Matcher;

@SuppressWarnings("unchecked")
public interface PerformsChecks {
  <T> void checksThat(T actual, Matcher<? super T>... matchers);

  <T, E extends AbstractObjectAssert<E, T>> E assertsThat(T actual);

  <T, E extends AbstractObjectAssert<E, T>> E assertsThat(Question<T> question);

  <T> void expects(Question<T> question, Matcher<? super T>... matchers);

  <T> PerformsChecks should(T actual, Matcher<? super T>... matchers);

  PerformsChecks should(Consequence consequence);

  <T, E extends AbstractObjectAssert<E, T>> E should(T actual);

  <T, E extends AbstractObjectAssert<E, T>> E should(Question<T> question);
}
