package pom.automationpractice.models;

import org.openqa.selenium.WebDriver;
import framework.web.pom.page.BasePage;
import pom.automationpractice.views.AuthenticationView;

public class AuthenticationPage extends BasePage {

    private final AuthenticationView view = new AuthenticationView(this.driver);

    public AuthenticationPage(WebDriver driver) { super(driver); }

    public AuthenticationPage enterIntoEmailInput(String email) {
        this.view.getEmailInput().insert(email);
        return this;
    }

    public AuthenticationPage clickCreateAccountButton() {
        this.view.getCreateAccountButton().click();
        return this;
    }

    public AuthenticationPage enterIntoFirstNameInput(String name) {
        this.view.getFirstNameInput().insert(name);
        return this;
    }

    public AuthenticationPage enterIntoLastNameInput(String name) {
        this.view.getLastNameInput().insert(name);
        return this;
    }

    public AuthenticationPage enterIntoPasswordInput(String password) {
        this.view.getPasswordInput().insert(password);
        return this;
    }

    public AuthenticationPage enterIntoAddressInput(String address) {
        this.view.getAddressInput().insert(address);
        return this;
    }

    public AuthenticationPage enterIntoCityInput(String city) {
        this.view.getCityInput().insert(city);
        return this;
    }

    public AuthenticationPage enterIntoPostalCodeInput(String code) {
        this.view.getPostcodeInput().insert(code);
        return this;
    }

    public AuthenticationPage enterIntoMobilePhoneInput(String code) {
        this.view.getMobilePhoneInput().insert(code);
        return this;
    }

    public AuthenticationPage enterIntoAliasInput(String alias) {
        this.view.getAliasInput().insert(alias);
        return this;
    }

    public AuthenticationPage selectStateFromDropDown(String state) {
        this.view.getStateSelect().select(state);
        return this;
    }

    public AuthenticationPage selectCountryFromDropDown(String country) {
        this.view.getCountrySelect().select(country);
        return this;
    }

    public AccountPage clickRegisterButton() {
        this.view.getRegisterButton().click();
        return new AccountPage(this.driver);
    }
}
