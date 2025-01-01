package framework.screenplay.actor;

import framework.screenplay.Interaction;

public interface PerformsInteractions {
  PerformsInteractions attemptsTo(Interaction... interactions);

  PerformsInteractions wasAbleTo(Interaction... interactions);

  PerformsInteractions has(Interaction... interactions);

  PerformsInteractions was(Interaction... interactions);

  PerformsInteractions is(Interaction... interactions);
}
