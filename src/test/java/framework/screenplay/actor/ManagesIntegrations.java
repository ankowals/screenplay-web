package framework.screenplay.actor;

import framework.screenplay.Integration;

public interface ManagesIntegrations {
    <T> T should (Integration<T> integration);
}
