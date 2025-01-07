package screenplay.automationpractice.interactions;

import framework.screenplay.Interaction;
import framework.screenplay.actor.Actor;
import framework.web.screenplay.BrowseTheWeb;
import pom.automationpractice.models.HomePage;
import testdata.AccountFormData;

public class Create {

  public static Interaction<Actor> account(AccountFormData accountFormData) {
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
