package framework.web.pom.elements.common;

import framework.web.pom.elements.Button;
import org.openqa.selenium.WebElement;

public class ButtonImpl implements Button {

    private final WebElement webElement;

    public ButtonImpl(WebElement webElement) {
        this.webElement = webElement;
    }

    @Override
    public void click() {
        this.webElement.click();
    }

    @Override
    public String getText() {
        return this.webElement.getAttribute("title");
    }

    public static ButtonImpl of(WebElement webElement) {
        return new ButtonImpl(webElement);
    }
}
