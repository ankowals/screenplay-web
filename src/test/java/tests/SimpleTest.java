package tests;

import base.TestBase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pom.automationpractice.models.AccountPage;
import pom.automationpractice.models.ProductDetailsPage;
import pom.automationpractice.models.SearchResultsPage;
import screenplay.BrowseTheWeb;
import screenplay.automationpractice.interactions.*;
import screenplay.automationpractice.questions.PageProperties;
import screenplay.automationpractice.questions.ProductDetails;
import screenplay.Open;
import testdata.AccountFormData;

import java.util.*;
import java.util.stream.Stream;

import static io.qala.datagen.RandomShortApi.english;
import static io.qala.datagen.RandomShortApi.numeric;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static framework.screenplay.helpers.Bdd.given;
import static framework.screenplay.helpers.Bdd.then;
import static framework.screenplay.helpers.Bdd.when;
import static framework.screenplay.helpers.SeeThat.seeThat;
import static screenplay.PageUrl.AUTOMATION_PRACTICE_HOME;

public class SimpleTest extends TestBase {

    @ParameterizedTest
    @MethodSource("accountFormDataProvider")
    void shouldCreateAccount(AccountFormData data) {
        given(user).can(BrowseTheWeb.with(browser));
        when(user).attemptsTo(
                Open.browser(AUTOMATION_PRACTICE_HOME),
                CreateAccount.with(data)
        );
        then(user).should(seeThat(PageProperties.title(AccountPage.class), equalTo("My account - My Store")));
    }

    @Test
    void shouldSearchForProduct() {
        given(user).can(BrowseTheWeb.with(browser));
        when(user).attemptsTo(
                Open.browser(AUTOMATION_PRACTICE_HOME),
                FindProductDetails.of("Printed Chiffon Dress")
        );
        then(user).should(seeThat(ProductDetails.getDetails()))
                .extracting(testdata.ProductDetails::getPrice)
                .isEqualTo("$16.40");
    }

    @Test
    void shouldSearchForProductButSmarter() {
        given(user).can(BrowseTheWeb.with(browser));
        when(user).attemptsTo(
                Open.browser(AUTOMATION_PRACTICE_HOME + "?controller=search&orderby=position&orderway=desc&search_query=dress&submit_search=", SearchResultsPage.class),
                ViewProductDetails.of("Printed Chiffon Dress")
        );
        then(user).should(seeThat(ProductDetails.getDetails(), hasProperty("price", equalTo("$17.40"))));
    }

    @Test
    void shouldContainTableDataSheetInProductDetails() {
        given(user).can(BrowseTheWeb.with(browser));
        when(user).attemptsTo(
                Open.browser(AUTOMATION_PRACTICE_HOME + "?id_product=7&controller=product&search_query=Printed+Chiffon+Dress&results=2", ProductDetailsPage.class)
        );
        then(user).should(seeThat(ProductDetails.getDataSheet())).asList()
                .contains(Collections.singletonMap("Compositions", "Polyester"))
                .contains(Collections.singletonMap("Styles", "Girly"))
                .contains(Collections.singletonMap("Properties", "Midi Dress"));
    }

    private static Stream<Arguments> accountFormDataProvider() {
        AccountFormData data = new AccountFormData.Builder()
                .with($ -> {
                    $.email = english(11) + "@terefere.terefere";
                    $.firstName = english(3, 8);
                    $.lastName = english(3, 8);
                    $.password = "admin";
                    $.address = english(11);
                    $.city = "New York";
                    $.state = "Alaska";
                    $.postalCode = numeric(5);
                    $.country = "United States";
                    $.mobilePhone = numeric(10);
                    $.alias = english(7);
                }).create();

        return Stream.of(Arguments.of(data));
    }
}
