package framework.screenplay.actor;

import framework.screenplay.Fact;

public interface ManagesFacts {
    ManagesFacts has(Fact... facts);
    ManagesFacts was(Fact... facts);
    ManagesFacts is(Fact... facts);
}
