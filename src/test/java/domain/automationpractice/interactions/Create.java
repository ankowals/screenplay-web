package domain.automationpractice.interactions;

import domain.BrowseTheWeb;
import domain.automationpractice.model.AccountFormData;
import domain.automationpractice.pom.models.AuthenticationPage;
import domain.automationpractice.pom.models.AutomationPracticeHomePage;
import framework.screenplay.Interaction;
import java.util.function.Consumer;

public class Create {

  public static Interaction account(AccountFormData accountFormData) {
    return actor ->
        BrowseTheWeb.as(actor)
            .onPage(AutomationPracticeHomePage.class)
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

  public static Interaction account(Consumer<AuthenticationPage> customizer) {
    return actor ->
        customizer.accept(
            BrowseTheWeb.as(actor).onPage(AutomationPracticeHomePage.class).clickSignInButton());
  }
}
