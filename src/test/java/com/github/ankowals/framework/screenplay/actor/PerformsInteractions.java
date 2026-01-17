package com.github.ankowals.framework.screenplay.actor;

import com.github.ankowals.framework.screenplay.Interaction;
import com.github.ankowals.framework.screenplay.Question;

public interface PerformsInteractions {
  void attemptsTo(Interaction... interactions);

  <T> T asksFor(Question<T> question) throws Exception;
}
