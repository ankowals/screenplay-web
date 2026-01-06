package tests;

import static framework.screenplay.helpers.Bdd.given;
import static framework.screenplay.helpers.Bdd.then;
import static framework.screenplay.helpers.Bdd.when;

import base.TestBase;
import domain.BrowseTheWeb;
import domain.automationpractice.interactions.*;
import domain.automationpractice.interactions.Open;
import domain.automationpractice.model.AccountFormData;
import domain.automationpractice.model.Product;
import domain.automationpractice.pom.models.AccountPage;
import domain.automationpractice.pom.models.ProductDetailsPage;
import domain.automationpractice.pom.models.SearchResultsPage;
import domain.automationpractice.questions.ThePage;
import domain.automationpractice.questions.TheProduct;
import framework.screenplay.actor.Actor;
import framework.screenplay.actor.Actors;
import framework.screenplay.helpers.See;
import java.util.*;
import java.util.stream.Stream;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Disabled("Page not available any more")
class AutomationPracticeTest extends TestBase {

  private Actor user;

  @BeforeEach
  void beforeEach() {
    this.user = Actors.withAbilities();
    this.user.can(BrowseTheWeb.with(this.browser));
  }

  static final String AUTOMATION_PRACTICE_HOME = "http://automationpractice.com/index.php";

  @ParameterizedTest
  @MethodSource("accountFormDataProvider")
  void shouldCreateAccount(AccountFormData data) throws Exception {
    given(this.user).can(BrowseTheWeb.with(this.browser));
    when(this.user).attemptsTo(Open.automationPractice(), Create.account(data));
    then(this.user)
        .should(
            See.that(ThePage.title(AccountPage.class), Matchers.equalTo("My account - My Store")));
  }

  @Test
  void shouldSearchForProduct() throws Exception {
    given(this.user).can(BrowseTheWeb.with(this.browser));
    when(this.user).attemptsTo(Open.automationPractice(), Find.product("Printed Chiffon Dress"));
    then(this.user).should(See.that(TheProduct.details())).returns("$16.40", Product::getPrice);
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
            View.product("Printed Chiffon Dress"));
    then(this.user)
        .should(
            See.that(
                TheProduct.details(), Matchers.hasProperty("price", Matchers.equalTo("$17.40"))));
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
        .should(See.that(TheProduct.dataSheet()))
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
                $ -> { // NOSONAR
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
