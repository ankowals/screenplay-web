package framework.screenplay.actor;

import org.hamcrest.Matcher;

@SuppressWarnings("unchecked")
public interface PerformsChecks {
  <T> void checksThat(T actual, Matcher<? super T>... matchers);
}
