package screenplay.interactions;

import io.qameta.allure.Step;
import pom.models.HomePage;
import screenplay.abilities.BrowseTheWeb;
import screenplay.framework.Interaction;

public class Open {

    @Step("Open browser at {url}")
    public static Interaction browserAt(String url) {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(HomePage.class)
                .open(url);
    }
}
