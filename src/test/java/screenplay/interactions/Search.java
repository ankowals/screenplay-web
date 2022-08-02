package screenplay.interactions;

import pom.models.HomePage;
import screenplay.abilities.BrowseTheWeb;
import screenplay.framework.Interaction;

public class Search {

    public static Interaction forText(String text) {
        return actor -> BrowseTheWeb.as(actor).onPage(HomePage.class)
                .enterIntoSearchInput(text)
                .clickSearchButton();
    }
}
