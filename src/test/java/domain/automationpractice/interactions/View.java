package domain.automationpractice.interactions;

import domain.automationpractice.pom.models.SearchResultsPage;
import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;

public class View {

  public static Interaction product(String product) {
    return actor ->
        BrowseTheWeb.as(actor).onPage(SearchResultsPage.class).clickProductLinkButton(product);
  }
}
