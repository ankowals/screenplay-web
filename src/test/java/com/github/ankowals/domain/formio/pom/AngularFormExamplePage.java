package com.github.ankowals.domain.formio.pom;

import com.github.ankowals.framework.web.pom.page.BasePage;
import org.openqa.selenium.WebDriver;

public class AngularFormExamplePage extends BasePage {

  private final AngularFormExampleView view = new AngularFormExampleView(this.driver);

  public AngularFormExamplePage(WebDriver driver) {
    super(driver);
  }

  public AngularFormExamplePage open() {
    this.open("https://formio.github.io/angular-demo");
    return this;
  }

  public AngularFormExamplePage enterFirstName(String firstName) {
    this.view.firstNameInput().insert(firstName);
    return this;
  }

  public void clickSubmit() {
    this.view.submitButton().click();
  }

  public String getSubmitMessage() {
    return this.view.submitMessage().getText();
  }
}
