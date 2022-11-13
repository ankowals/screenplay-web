package framework.screenplay.actor;

import framework.screenplay.Interaction;

public interface PerformsInteractions {
    PerformsInteractions attemptsTo(Interaction... interactions);
    PerformsInteractions wasAbleTo(Interaction... interactions);
}
