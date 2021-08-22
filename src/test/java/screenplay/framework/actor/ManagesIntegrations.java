package screenplay.framework.actor;

import screenplay.framework.Integration;

public interface ManagesIntegrations {
    <T> T should (Integration<T> integration);
}
