package framework.web.pom.elements;

import java.util.List;

public interface Dropdown {
    void select(String value);
    List<String> getOptions();
}
