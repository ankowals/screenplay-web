package framework.web.pom.elements;

import org.openqa.selenium.*;

public class InputImpl extends ElementImpl implements Input {

    public InputImpl(WebElement element) {
        super(element);
    }

    @Override
    public void insert(String text) {
        this.clear();
        this.sendKeys(text);
    }

    @Override
    public String getText() {
        return this.getAttribute("value");
    }

    public static InputImpl of(WebElement element) {
        return new InputImpl(element);
    }
}
