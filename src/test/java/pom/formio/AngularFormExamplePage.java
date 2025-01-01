package pom.formio;

import framework.web.pom.page.BasePage;
import org.openqa.selenium.WebDriver;

public class AngularFormExamplePage extends BasePage {

  private final AngularFormExampleView view = new AngularFormExampleView(this.driver);

  public AngularFormExamplePage(WebDriver driver) {
    super(driver);
  }

  public AngularFormExamplePage enterFirstName(String firstName) {
    this.view.getFirstNameInput().insert(firstName);

    return this;
  }

  public void clickSubmit() {
    this.view.getSubmitButton().click();
  }

  public String getSubmitMessageText() {
    return this.view.getSubmitMessage().getText();
  }
}
