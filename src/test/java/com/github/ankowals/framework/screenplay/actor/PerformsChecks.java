package com.github.ankowals.framework.screenplay.actor;

import com.github.ankowals.framework.screenplay.Consequence;

public interface PerformsChecks {
  void should(Consequence... consequences) throws Exception;
}
