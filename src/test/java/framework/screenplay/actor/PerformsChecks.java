package framework.screenplay.actor;

import framework.screenplay.Consequence;
import org.assertj.core.api.ObjectAssert;

public interface PerformsChecks {
  <T> ObjectAssert<T> assertsThat(T actual);

  void should(Consequence consequence) throws Exception;
}
