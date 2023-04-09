package framework.screenplay.actor;

import framework.screenplay.Integration;

@FunctionalInterface
public interface ManagesIntegrations {
    <T> T should (Integration<T> integration);
}
