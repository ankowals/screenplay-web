package screenplay.automationpractice.interactions;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import java.util.function.Consumer;
import pom.automationpractice.models.AuthenticationPage;
import pom.automationpractice.models.AutomationPracticeHomePage;
import screenplay.automationpractice.model.AccountFormData;

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
