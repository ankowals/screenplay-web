package pom.formio;

import framework.web.pom.elements.ButtonImpl;
import framework.web.pom.elements.ElementImpl;
import framework.web.pom.elements.InputImpl;
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

    public InputImpl getFirstNameInput() {
        WebElement element = this.wait.until(visibilityOfElementLocated(By.xpath("//*[@name='data[firstName]']")));
        return InputImpl.of(element);
    }

    public ButtonImpl getSubmitButton() {
        WebElement element = this.wait.until(elementToBeClickable(By.xpath("//*[@name='data[submit]']")));
        return ButtonImpl.of(element);
    }

    public ElementImpl getSubmitMessage() {
        WebElement element = this.wait.until(visibilityOfElementLocated(By.xpath("//*[@ref='buttonMessage']")));
        return ElementImpl.of(element);
    }
}
