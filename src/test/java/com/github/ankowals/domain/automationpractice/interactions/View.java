package com.github.ankowals.domain.automationpractice.interactions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.automationpractice.pom.models.SearchResultsPage;
import com.github.ankowals.framework.screenplay.Interaction;

public class View {

  public static Interaction product(String product) {
    return actor ->
        BrowseTheWeb.as(actor).onPage(SearchResultsPage.class).clickProductLinkButton(product);
  }
}
