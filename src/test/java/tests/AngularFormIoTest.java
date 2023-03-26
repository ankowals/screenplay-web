package tests;

import base.TestBase;
import framework.screenplay.Interaction;
import framework.screenplay.Question;
import framework.screenplay.actor.Actor;
import framework.web.pom.elements.ButtonImpl;
import framework.web.pom.elements.ElementImpl;
import framework.web.pom.elements.InputImpl;
import framework.web.pom.page.BasePage;
import framework.web.pom.page.BaseView;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import screenplay.abilities.BrowseTheWeb;
import screenplay.interactions.Open;

import static framework.screenplay.helpers.Bdd.*;
import static framework.screenplay.helpers.SeeThat.seeThat;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.containsString;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static screenplay.PageUrl.FORM_IO_DEMO;

public class AngularFormIoTest extends TestBase {

    @Test
    void shouldSubmitForm() {
        Actor user = new Actor();

        given(user).can(BrowseTheWeb.with(browser));
        when(user).attemptsTo(
                Open.browserAt(FORM_IO_DEMO),
                Enter.firstName(randomAlphabetic(8)),
                Submit.form()
        );
        then(user).should(seeThat(Submit.message(), containsString("Submission Complete")));
    }

    public static class Enter {
        public static Interaction firstName(String firstName) {
            return actor -> BrowseTheWeb.as(actor)
                    .onPage(AngularFormExamplePage.class)
                    .enterFirstName(firstName);
        }
    }

    public static class Submit {
        public static Interaction form() {
            return actor -> BrowseTheWeb.as(actor)
                    .onPage(AngularFormExamplePage.class)
                    .clickSubmit();
        }

        public static Question<String> message() {
            return actor -> BrowseTheWeb.as(actor)
                    .onPage(AngularFormExamplePage.class)
                    .getSubmitMessageText();
        }
    }

    public static class AngularFormExamplePage extends BasePage {

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

    public static class AngularFormExampleView extends BaseView {

        public AngularFormExampleView(WebDriver driver) {
            super(driver);
        }

        public InputImpl getFirstNameInput() {
            WebElement element = wait.until(visibilityOfElementLocated(By.xpath("//*[@name='data[firstName]']")));
            return InputImpl.of(element);
        }

        public ButtonImpl getSubmitButton() {
            WebElement element = wait.until(elementToBeClickable(By.xpath("//*[@name='data[submit]']")));
            return ButtonImpl.of(element);
        }

        public ElementImpl getSubmitMessage() {
            WebElement element = wait.until(visibilityOfElementLocated(By.xpath("//*[@ref='buttonMessage']")));
            return ElementImpl.of(element);
        }
    }

}