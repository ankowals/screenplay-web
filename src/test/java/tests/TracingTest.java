package tests;

import base.TestBase;
import framework.screenplay.actor.Actor;
import org.junit.jupiter.api.Test;
import screenplay.abilities.BrowseTheWeb;
import screenplay.interactions.Open;
import screenplay.parasoft.ParasoftProducts;

import static framework.screenplay.helpers.Bdd.*;
import static framework.screenplay.helpers.SeeThat.seeThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static screenplay.PageUrl.PARASOFT_PRODUCTS;

public class TracingTest extends TestBase {

    @Test
    void shouldTraceInteractions() {
        Actor user = new Actor();

        given(user).can(BrowseTheWeb.with(browser));
        when(user).attemptsTo(
                Open.browserAt(PARASOFT_PRODUCTS)
        );
        then(user).should(seeThat(ParasoftProducts.title(), equalTo("PRODUCTS")));
    }
}
