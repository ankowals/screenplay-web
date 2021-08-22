package pom.models;

import org.openqa.selenium.WebDriver;
import pom.framework.page.BasePage;
import pom.views.AuthenticationView;

public class AuthenticationPage extends BasePage {

    private final AuthenticationView view = new AuthenticationView(driver);

    public AuthenticationPage(WebDriver driver) { super(driver); }

    public AuthenticationPage enterIntoEmailInput(String email) {
        view.getEmailInput().insert(email);
        return this;
    }

    public AuthenticationPage clickCreateAccountButton() {
        view.getCreateAccountButton().click();
        return this;
    }

    public AuthenticationPage enterIntoFirstNameInput(String name) {
        view.getFirstNameInput().insert(name);
        return this;
    }

    public AuthenticationPage enterIntoLastNameInput(String name) {
        view.getLastNameInput().insert(name);
        return this;
    }

    public AuthenticationPage enterIntoPasswordInput(String password) {
        view.getPasswordInput().insert(password);
        return this;
    }

    public AuthenticationPage enterIntoAddressInput(String address) {
        view.getAddressInput().insert(address);
        return this;
    }

    public AuthenticationPage enterIntoCityInput(String city) {
        view.getCityInput().insert(city);
        return this;
    }

    public AuthenticationPage enterIntoPostalCodeInput(String code) {
        view.getPostcodeInput().insert(code);
        return this;
    }

    public AuthenticationPage enterIntoMobilePhoneInput(String code) {
        view.getMobilePhoneInput().insert(code);
        return this;
    }

    public AuthenticationPage enterIntoAliasInput(String alias) {
        view.getAliasInput().insert(alias);
        return this;
    }

    public AuthenticationPage selectStateFromDropDown(String state) {
        view.getStateSelect().select(state);
        return this;
    }

    public AuthenticationPage selectCountryFromDropDown(String country) {
        view.getCountrySelect().select(country);
        return this;
    }

    public AccountPage clickRegisterButton() {
        view.getRegisterButton().click();
        return new AccountPage(driver);
    }
}
