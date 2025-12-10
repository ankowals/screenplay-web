package pom.saucedemo;

import framework.web.pom.elements.Button;
import framework.web.pom.elements.Element;
import framework.web.pom.elements.Input;
import framework.web.pom.elements.common.ButtonImpl;
import framework.web.pom.elements.common.ElementImpl;
import framework.web.pom.elements.common.InputImpl;
import framework.web.pom.page.BasePage;
import framework.web.pom.page.BaseView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

  private final LoginView view = new LoginView(this.driver);

  public LoginPage(WebDriver driver) {
    super(driver);
  }

  public LoginPage enterUsername(String username) {
    this.view.getUsernameInput().clear();
    this.view.getUsernameInput().insert(username);
    return this;
  }

  public LoginPage open() {
    this.open("https://www.saucedemo.com/");
    this.awaitUntilLoaded();
    return this;
  }

  public LoginPage enterPassword(String password) {
    this.view.getPasswordInput().clear();
    this.view.getPasswordInput().insert(password);
    return this;
  }

  public ProductsPage clickLogin() {
    this.view.getLoginButton().click();
    return new ProductsPage(this.driver);
  }

  public String getErrorText() {
    return this.view.getErrorElement().getText();
  }

  private void awaitUntilLoaded() {
    this.view.getUsernameInput();
  }

  static class LoginView extends BaseView {

    LoginView(WebDriver driver) {
      super(driver);
    }

    Input getUsernameInput() {
      return InputImpl.of(
          this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-name"))));
    }

    Input getPasswordInput() {
      return InputImpl.of(
          this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password"))));
    }

    Button getLoginButton() {
      return ButtonImpl.of(
          this.wait.until(ExpectedConditions.elementToBeClickable(By.id("login-button"))));
    }

    Element getErrorElement() {
      return ElementImpl.of(
          this.wait.until(
              ExpectedConditions.visibilityOfElementLocated(
                  By.xpath(
                      "//*[@id='login_button_container']/descendant::h3[@data-test='error']"))));
    }
  }
}
