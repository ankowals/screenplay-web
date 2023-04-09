package framework.web.pom.elements;

import org.openqa.selenium.WebElement;

public class ButtonImpl extends ElementImpl implements Button {

    public ButtonImpl(WebElement element) {
        super(element);
    }

    @Override
    public String getText() {
        return this.getAttribute("title");
    }

    public static ButtonImpl of(WebElement element) {
        return new ButtonImpl(element);
    }
}
