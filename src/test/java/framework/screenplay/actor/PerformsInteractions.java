package framework.screenplay.actor;

import framework.screenplay.Interaction;
import framework.screenplay.Question;

public interface PerformsInteractions {
  <T extends Interaction> void attemptsTo(T... interactions);

  <T> T asksFor(Question<T> question);
}
