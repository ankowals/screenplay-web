package screenplay.automationpractice.interactions;

import pom.automationpractice.models.HomePage;
import framework.web.screenplay.BrowseTheWeb;
import framework.screenplay.Interaction;

public class Search {

    public static Interaction forText(String text) {
        return actor -> BrowseTheWeb.as(actor).onPage(HomePage.class)
                .enterIntoSearchInput(text)
                .clickSearchButton();
    }
}
