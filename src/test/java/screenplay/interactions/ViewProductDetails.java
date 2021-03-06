package screenplay.interactions;

import pom.models.SearchResultsPage;
import screenplay.abilities.BrowseTheWeb;
import screenplay.framework.Interaction;

public class ViewProductDetails {

    public static Interaction of(String product) {
        return actor -> BrowseTheWeb.as(actor).onPage(SearchResultsPage.class)
                .clickProductLinkButton(product);
    }
}
