package screenplay.automationpractice.interactions;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import pom.automationpractice.models.HomePage;

public class Search {

  public static Interaction forText(String text) {
    return actor ->
        BrowseTheWeb.as(actor)
            .onPage(HomePage.class)
            .enterIntoSearchInput(text)
            .clickSearchButton();
  }
}
