package screenplay.automationpractice.interactions;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import pom.automationpractice.models.HomePage;
import testdata.AccountFormData;

public class CreateAccount {

  public static Interaction with(AccountFormData accountFormData) {
    return actor ->
        BrowseTheWeb.as(actor)
            .onPage(HomePage.class)
            .clickSignInButton()
            .enterIntoEmailInput(accountFormData.getEmail())
            .clickCreateAccountButton()
            .enterIntoFirstNameInput(accountFormData.getFirstName())
            .enterIntoLastNameInput(accountFormData.getLastName())
            .enterIntoPasswordInput(accountFormData.getPassword())
            .enterIntoAddressInput(accountFormData.getAddress())
            .enterIntoCityInput(accountFormData.getCity())
            .selectStateFromDropDown(accountFormData.getState())
            .enterIntoPostalCodeInput(accountFormData.getPostalCode())
            .selectCountryFromDropDown(accountFormData.getCountry())
            .enterIntoMobilePhoneInput(accountFormData.getMobilePhone())
            .enterIntoAliasInput(accountFormData.getAlias())
            .clickRegisterButton();
  }
}
