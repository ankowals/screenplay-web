package framework.screenplay.actor;

import framework.screenplay.Interaction;
import framework.screenplay.Question;

public interface PerformsInteractions {
  void attemptsTo(Interaction<Actor>... interactions);

  <T> T asksFor(Question<T, Actor> question) throws Exception;
}
