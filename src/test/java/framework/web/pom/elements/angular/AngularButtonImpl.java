package framework.web.pom.elements.angular;

import framework.web.pom.conditions.AngularExpectedConditions;
import framework.web.pom.elements.Button;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AngularButtonImpl extends AbstractAngularElement implements Button {

    public AngularButtonImpl(WebElement webElement, WebDriverWait webDriverWait) {
        super(webElement, webDriverWait);
    }

    @Override
    public void click() {
        this.webElement.click();
        this.waitUntil(AngularExpectedConditions.contentLoaded());
    }

    @Override
    public String getText() {
        return this.webElement.getAttribute("title");
    }

    public static AngularButtonImpl of(WebElement webElement, WebDriverWait webDriverWait) {
        return new AngularButtonImpl(webElement, webDriverWait);
    }
}
