package pom.formio;

import framework.web.pom.page.BasePage;
import org.openqa.selenium.WebDriver;
import tests.AngularFormIoTest;

public class AngularFormExamplePage extends BasePage {

    private final AngularFormExampleView view = new AngularFormExampleView(driver);

    public AngularFormExamplePage(WebDriver driver) {
        super(driver);
    }

    public AngularFormExamplePage enterFirstName(String firstName) {
        view.getFirstNameInput().clear();
        view.getFirstNameInput().sendKeys(firstName);

        return this;
    }

    public void clickSubmit() {
        view.getSubmitButton().click();
    }

    public String getSubmitMessageText() {
        return view.getSubmitMessage().getText();
    }
}
