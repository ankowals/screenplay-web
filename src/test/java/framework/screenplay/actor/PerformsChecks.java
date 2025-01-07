package framework.screenplay.actor;

import framework.screenplay.Consequence;
import org.assertj.core.api.AbstractObjectAssert;

public interface PerformsChecks {
  <T, E extends AbstractObjectAssert<E, T>> E assertsThat(T actual);

  void should(Consequence<Actor> consequence) throws Exception;
}
