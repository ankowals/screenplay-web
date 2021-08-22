package screenplay.framework.actor;

import screenplay.framework.Fact;

public interface ManagesFacts {
    ManagesFacts has(Fact... facts);
    ManagesFacts was(Fact... facts);
    ManagesFacts is(Fact... facts);
}
