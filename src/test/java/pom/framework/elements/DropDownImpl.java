package pom.framework.elements;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class DropDownImpl extends ElementImpl implements Dropdown {

    public DropDownImpl(WebElement element) {
        super(element);
    }

    @Override
    public void select(String value) {
        Select dropdown = new Select(element);
        dropdown.selectByVisibleText(value);
    }

    public static DropDownImpl of(WebElement element) {
        return new DropDownImpl(element);
    }
}
