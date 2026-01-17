package com.github.ankowals.domain.saucedemo.pom;

import com.github.ankowals.framework.web.pom.elements.Button;
import com.github.ankowals.framework.web.pom.elements.Input;
import com.github.ankowals.framework.web.pom.elements.common.ButtonImpl;
import com.github.ankowals.framework.web.pom.elements.common.InputImpl;
import com.github.ankowals.framework.web.pom.page.BasePage;
import com.github.ankowals.framework.web.pom.page.BaseView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutYourInformationPage extends BasePage {

  CheckoutYourInformationView view = new CheckoutYourInformationView(this.driver);
  HeaderView headerView = new HeaderView(this.driver);

  public CheckoutYourInformationPage(WebDriver driver) {
    super(driver);
  }

  @Override
  public String getTitle() {
    return this.headerView.titleElement().getText();
  }

  public CheckoutOverviewPage clickContinue() {
    this.view.getContinueButton().click();
    return new CheckoutOverviewPage(this.driver);
  }

  public CartPage clickCancel() {
    this.view.getCancelButton().click();
    return new CartPage(this.driver);
  }

  public CheckoutYourInformationPage enterFirstName(String text) {
    this.view.getFirstNameInput().clear();
    this.view.getFirstNameInput().insert(text);
    return this;
  }

  public CheckoutYourInformationPage enterLastName(String text) {
    this.view.getLastNameInput().clear();
    this.view.getLastNameInput().insert(text);
    return this;
  }

  public CheckoutYourInformationPage enterPostalCode(String text) {
    this.view.getPostalCodeInput().clear();
    this.view.getPostalCodeInput().insert(text);
    return this;
  }

  static class CheckoutYourInformationView extends BaseView {

    CheckoutYourInformationView(WebDriver driver) {
      super(driver);
    }

    Button getContinueButton() {
      return ButtonImpl.of(
          this.wait.until(ExpectedConditions.elementToBeClickable(By.id("continue"))));
    }

    Button getCancelButton() {
      return ButtonImpl.of(
          this.wait.until(ExpectedConditions.elementToBeClickable(By.id("cancel"))));
    }

    Input getFirstNameInput() {
      return InputImpl.of(
          this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name"))));
    }

    Input getLastNameInput() {
      return InputImpl.of(
          this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("last-name"))));
    }

    Input getPostalCodeInput() {
      return InputImpl.of(
          this.wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("postal-code"))));
    }
  }
}
