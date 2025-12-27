package framework.screenplay.actor;

import framework.screenplay.Consequence;
import framework.screenplay.Question;
import org.assertj.core.api.ObjectAssert;

public interface PerformsChecks {
  void should(Consequence... consequences) throws Exception;

  <T> ObjectAssert<T> should(Question<T> question) throws Exception;
}
