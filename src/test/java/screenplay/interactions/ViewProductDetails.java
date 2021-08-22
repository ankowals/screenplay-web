package screenplay.interactions;

import io.qameta.allure.Step;
import pom.models.SearchResultsPage;
import screenplay.abilities.BrowseTheWeb;
import screenplay.framework.Interaction;

public class ViewProductDetails {

    @Step("View Product Details of {product}")
    public static Interaction of(String product) {
        return actor -> BrowseTheWeb.as(actor).onPage(SearchResultsPage.class)
                .clickProductLinkButton(product);
    }
}
