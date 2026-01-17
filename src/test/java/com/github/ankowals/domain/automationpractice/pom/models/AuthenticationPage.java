package com.github.ankowals.domain.automationpractice.pom.models;

import com.github.ankowals.domain.automationpractice.pom.views.AuthenticationView;
import com.github.ankowals.framework.web.pom.page.BasePage;
import org.openqa.selenium.WebDriver;

public class AuthenticationPage extends BasePage {

  private final AuthenticationView view = new AuthenticationView(this.driver);

  public AuthenticationPage(WebDriver driver) {
    super(driver);
  }

  public AuthenticationPage enterIntoEmailInput(String email) {
    this.view.emailInput().insert(email);
    return this;
  }

  public AuthenticationPage clickCreateAccountButton() {
    this.view.createAccountButton().click();
    return this;
  }

  public AuthenticationPage enterIntoFirstNameInput(String name) {
    this.view.firstNameInput().insert(name);
    return this;
  }

  public AuthenticationPage enterIntoLastNameInput(String name) {
    this.view.lastNameInput().insert(name);
    return this;
  }

  public AuthenticationPage enterIntoPasswordInput(String password) {
    this.view.passwordInput().insert(password);
    return this;
  }

  public AuthenticationPage enterIntoAddressInput(String address) {
    this.view.addressInput().insert(address);
    return this;
  }

  public AuthenticationPage enterIntoCityInput(String city) {
    this.view.cityInput().insert(city);
    return this;
  }

  public AuthenticationPage enterIntoPostalCodeInput(String code) {
    this.view.postcodeInput().insert(code);
    return this;
  }

  public AuthenticationPage enterIntoMobilePhoneInput(String code) {
    this.view.mobilePhoneInput().insert(code);
    return this;
  }

  public AuthenticationPage enterIntoAliasInput(String alias) {
    this.view.aliasInput().insert(alias);
    return this;
  }

  public AuthenticationPage selectStateFromDropDown(String state) {
    this.view.stateSelect().select(state);
    return this;
  }

  public AuthenticationPage selectCountryFromDropDown(String country) {
    this.view.countrySelect().select(country);
    return this;
  }

  public AccountPage clickRegisterButton() {
    this.view.registerButton().click();
    return new AccountPage(this.driver);
  }
}
