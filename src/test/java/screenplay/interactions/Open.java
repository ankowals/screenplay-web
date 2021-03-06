package screenplay.interactions;

import pom.models.HomePage;
import screenplay.abilities.BrowseTheWeb;
import screenplay.framework.Interaction;

public class Open {

    public static Interaction browserAt(String url) {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(HomePage.class)
                .open(url);
    }
}
