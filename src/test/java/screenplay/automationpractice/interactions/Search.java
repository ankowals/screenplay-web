package screenplay.automationpractice.interactions;

import framework.screenplay.Interaction;
import framework.screenplay.actor.Actor;
import framework.web.screenplay.BrowseTheWeb;
import pom.automationpractice.models.HomePage;

public class Search {

  public static Interaction<Actor> forText(String text) {
    return actor ->
        BrowseTheWeb.as(actor)
            .onPage(HomePage.class)
            .enterIntoSearchInput(text)
            .clickSearchButton();
  }
}
