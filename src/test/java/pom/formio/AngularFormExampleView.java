package pom.formio;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

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

class AngularFormExampleView extends BaseView {

  AngularFormExampleView(WebDriver driver) {
    super(driver);
  }

  Input firstNameInput() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//*[@name='data[firstName]']")));
    return InputImpl.of(element);
  }

  Button submitButton() {
    WebElement element =
        this.wait.until(elementToBeClickable(By.xpath("//*[@name='data[submit]']")));
    return ButtonImpl.of(element);
  }

  Element submitMessage() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//*[@ref='buttonMessage']")));
    return ElementImpl.of(element);
  }
}
