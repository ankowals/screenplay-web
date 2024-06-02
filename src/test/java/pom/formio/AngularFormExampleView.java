package pom.formio;

import framework.web.pom.elements.Button;
import framework.web.pom.elements.Element;
import framework.web.pom.elements.Input;
import framework.web.pom.elements.common.ButtonImpl;
import framework.web.pom.elements.common.ElementImpl;
import framework.web.pom.elements.common.InputImpl;
import framework.web.pom.page.BaseView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class AngularFormExampleView extends BaseView {

    public AngularFormExampleView(WebDriver driver) {
        super(driver);
    }

    public Input getFirstNameInput() {
        WebElement element = this.wait.until(visibilityOfElementLocated(By.xpath("//*[@name='data[firstName]']")));
        return InputImpl.of(element);
    }

    public Button getSubmitButton() {
        WebElement element = this.wait.until(elementToBeClickable(By.xpath("//*[@name='data[submit]']")));
        return ButtonImpl.of(element);
    }

    public Element getSubmitMessage() {
        WebElement element = this.wait.until(visibilityOfElementLocated(By.xpath("//*[@ref='buttonMessage']")));
        return ElementImpl.of(element);
    }
}
