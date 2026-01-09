package framework.screenplay.actor;

import framework.screenplay.Consequence;

public interface PerformsChecks {
  void should(Consequence... consequences) throws Exception;
}
