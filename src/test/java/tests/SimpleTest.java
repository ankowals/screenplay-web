package tests;

import base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pom.models.AccountPage;
import screenplay.abilities.BrowseTheWeb;
import screenplay.interactions.*;
import screenplay.framework.actor.Actor;
import screenplay.questions.PageProperties;
import screenplay.questions.ProductDetails;
import testdata.AccountFormData;

import java.util.*;
import java.util.stream.Stream;

import static io.qala.datagen.RandomShortApi.english;
import static io.qala.datagen.RandomShortApi.numeric;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static screenplay.framework.helpers.Bdd.given;
import static screenplay.framework.helpers.Bdd.then;
import static screenplay.framework.helpers.Bdd.when;
import static screenplay.framework.helpers.SeeThat.seeThat;

public class SimpleTest extends BaseTest {

    private static final String HOME_PAGE_URL = "http://automationpractice.com/index.php";

    @ParameterizedTest
    @MethodSource("accountFormDataProvider")
    void createAnAccount(AccountFormData data) {
        Actor user = new Actor();

        given(user).can(BrowseTheWeb.with(browser));
        when(user).attemptsTo(
                Open.browserAt(HOME_PAGE_URL),
                CreateAccount.with(data)
        );
        then(user).should(seeThat(PageProperties.title(AccountPage.class), equalTo("My account - My Store")));
    }

    @Test
    void searchForProduct() {
        Actor user = new Actor();

        given(user).can(BrowseTheWeb.with(browser));
        when(user).attemptsTo(
                Open.browserAt(HOME_PAGE_URL),
                FindProductDetails.of("Printed Chiffon Dress")
        );
        then(user).should(seeThat(ProductDetails.getDetails()))
                .extracting(testdata.ProductDetails::getPrice)
                .isEqualTo("$16.40");
    }

    @Test
    void smarterSearchForProduct() {
        Actor user = new Actor();

        given(user).can(BrowseTheWeb.with(browser));
        when(user).attemptsTo(
                Open.browserAt(HOME_PAGE_URL + "?controller=search&orderby=position&orderway=desc&search_query=dress&submit_search="),
                ViewProductDetails.of("Printed Chiffon Dress")
        );
        then(user).should(seeThat(ProductDetails.getDetails(), hasProperty("price", equalTo("$17.40"))));
    }

    @Test
    void productDetailsShouldContainTableDataSheet() {
        Actor user = new Actor();

        given(user).can(BrowseTheWeb.with(browser));
        when(user).attemptsTo(
                Open.browserAt(HOME_PAGE_URL + "?id_product=7&controller=product&search_query=Printed+Chiffon+Dress&results=2")
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
