package screenplay.automationpractice.interactions;

import framework.screenplay.Interaction;
import framework.screenplay.actor.Actor;
import framework.web.screenplay.BrowseTheWeb;
import pom.automationpractice.models.SearchResultsPage;

public class View {

  public static Interaction<Actor> productDetails(String product) {
    return actor ->
        BrowseTheWeb.as(actor).onPage(SearchResultsPage.class).clickProductLinkButton(product);
  }
}
