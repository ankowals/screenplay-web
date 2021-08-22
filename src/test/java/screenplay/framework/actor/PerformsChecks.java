package screenplay.framework.actor;

import screenplay.framework.Consequence;
import screenplay.framework.Question;
import org.assertj.core.api.AbstractObjectAssert;
import org.hamcrest.Matcher;

public interface PerformsChecks {
    <T> void checksThat(T actual, Matcher<? super T>... matchers);
    <T> PerformsChecks should(T actual, Matcher<? super T>... matchers);
    PerformsChecks should(Consequence consequence);
    <T, E extends AbstractObjectAssert<E, T>> E should(Question<T> question);
    <T, E extends AbstractObjectAssert<E, T>> E should(T actual);
    <T> void expects(Question<T> question, Matcher<? super T> matcher);
}
