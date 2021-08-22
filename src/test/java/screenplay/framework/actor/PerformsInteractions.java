package screenplay.framework.actor;

import screenplay.framework.Interaction;
import screenplay.framework.Question;

public interface PerformsInteractions {
    PerformsInteractions attemptsTo(Interaction... interactions);
    PerformsInteractions wasAbleTo(Interaction... interactions);
}
