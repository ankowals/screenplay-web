package framework.screenplay.actor;

import framework.screenplay.Interaction;
import framework.screenplay.Question;

public interface PerformsInteractions {
  void attemptsTo(Interaction... interactions);

  <T> T asksFor(Question<T> question) throws Exception;
}
