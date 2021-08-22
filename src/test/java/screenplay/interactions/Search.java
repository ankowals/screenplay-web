package screenplay.interactions;

import io.qameta.allure.Step;
import pom.models.HomePage;
import screenplay.abilities.BrowseTheWeb;
import screenplay.framework.Interaction;

public class Search {

    @Step("Search for text {text}")
    public static Interaction forText(String text) {
        return actor -> BrowseTheWeb.as(actor).onPage(HomePage.class)
                .enterIntoSearchInput(text)
                .clickSearchButton();
    }
}
