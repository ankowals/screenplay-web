package framework.pom.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.stream.Collectors;

public class DropDownImpl extends ElementImpl implements Dropdown {

    public DropDownImpl(WebElement element) {
        super(element);
    }

    @Override
    public void select(String value) {
        new Select(element).selectByVisibleText(value);
    }

    @Override
    public List<String> getOptions() {
        return new Select(element).getOptions().stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public static DropDownImpl of(WebElement element) {
        return new DropDownImpl(element);
    }
}
