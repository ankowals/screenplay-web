package screenplay.automationpractice.interactions;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import pom.automationpractice.models.SearchResultsPage;

public class ViewProductDetails {

  public static Interaction of(String product) {
    return actor ->
        BrowseTheWeb.as(actor).onPage(SearchResultsPage.class).clickProductLinkButton(product);
  }
}
