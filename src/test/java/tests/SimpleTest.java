package tests;

import static framework.screenplay.helpers.Bdd.given;
import static framework.screenplay.helpers.Bdd.then;
import static framework.screenplay.helpers.Bdd.when;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static screenplay.PageUrl.AUTOMATION_PRACTICE_HOME;

import base.TestBase;
import framework.screenplay.helpers.See;
import framework.web.screenplay.BrowseTheWeb;
import java.util.*;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pom.automationpractice.models.AccountPage;
import pom.automationpractice.models.ProductDetailsPage;
import pom.automationpractice.models.SearchResultsPage;
import screenplay.Open;
import screenplay.automationpractice.interactions.*;
import screenplay.automationpractice.questions.Page;
import screenplay.automationpractice.questions.ProductDetails;
import testdata.AccountFormData;

@Disabled("Page not available any more")
class SimpleTest extends TestBase {

  @ParameterizedTest
  @MethodSource("accountFormDataProvider")
  void shouldCreateAccount(AccountFormData data) throws Exception {
    given(this.user).can(BrowseTheWeb.with(this.browser));
    when(this.user).attemptsTo(Open.browser(AUTOMATION_PRACTICE_HOME), Create.account(data));
    then(this.user)
        .should(See.that(Page.title(AccountPage.class), equalTo("My account - My Store")));
  }

  // better alternative
  @ParameterizedTest
  @MethodSource("accountFormDataProvider")
  void shouldCreateSimplerAccount(AccountFormData accountFormData) throws Exception {
    given(this.user).can(BrowseTheWeb.with(this.browser));
    when(this.user)
        .attemptsTo(
            Open.browser(AUTOMATION_PRACTICE_HOME),
            Create.account(
                accountFormPage ->
                    accountFormPage
                        .enterIntoFirstNameInput(accountFormData.getFirstName())
                        .enterIntoLastNameInput(accountFormData.getLastName())
                        .enterIntoPasswordInput(accountFormData.getPassword())
                        .clickRegisterButton()));
    then(this.user)
        .should(See.that(Page.title(AccountPage.class), equalTo("My account - My Store")));
  }

  @Test
  void shouldSearchForProduct() throws Exception {
    given(this.user).can(BrowseTheWeb.with(this.browser));
    when(this.user)
        .attemptsTo(
            Open.browser(AUTOMATION_PRACTICE_HOME), Find.productDetails("Printed Chiffon Dress"));
    then(this.user)
        .should(See.that(ProductDetails.get()))
        .returns("$16.40", testdata.ProductDetails::getPrice);
  }

  @Test
  void shouldSearchForProductButSmarter() throws Exception {
    given(this.user).can(BrowseTheWeb.with(this.browser));
    when(this.user)
        .attemptsTo(
            Open.browser(
                AUTOMATION_PRACTICE_HOME
                    + "?controller=search&orderby=position&orderway=desc&search_query=dress&submit_search=",
                SearchResultsPage.class),
            View.productDetails("Printed Chiffon Dress"));
    then(this.user).should(See.that(ProductDetails.get(), hasProperty("price", equalTo("$17.40"))));
  }

  @Test
  void shouldContainTableDataSheetInProductDetails() throws Exception {
    given(this.user).can(BrowseTheWeb.with(this.browser));
    when(this.user)
        .attemptsTo(
            Open.browser(
                AUTOMATION_PRACTICE_HOME
                    + "?id_product=7&controller=product&search_query=Printed+Chiffon+Dress&results=2",
                ProductDetailsPage.class));
    then(this.user)
        .should(See.that(ProductDetails.dataSheet()))
        .asInstanceOf(InstanceOfAssertFactories.LIST)
        .contains(Collections.singletonMap("Compositions", "Polyester"))
        .contains(Collections.singletonMap("Styles", "Girly"))
        .contains(Collections.singletonMap("Properties", "Midi Dress"));
  }

  private static Stream<Arguments> accountFormDataProvider() {
    RandomStringUtils randomStringUtils = RandomStringUtils.insecure();
    AccountFormData data =
        new AccountFormData.Builder()
            .with(
                $ -> {
                  $.email = randomStringUtils.nextAlphabetic(11) + "@terefere.terefere";
                  $.firstName = randomStringUtils.nextAlphabetic(3, 8);
                  $.lastName = randomStringUtils.nextAlphabetic(3, 8);
                  $.password = "admin";
                  $.address = randomStringUtils.nextAlphabetic(11);
                  $.city = "New York";
                  $.state = "Alaska";
                  $.postalCode = randomStringUtils.nextNumeric(5);
                  $.country = "United States";
                  $.mobilePhone = randomStringUtils.nextNumeric(10);
                  $.alias = randomStringUtils.nextAlphabetic(7);
                })
            .create();

    return Stream.of(Arguments.of(data));
  }
}
