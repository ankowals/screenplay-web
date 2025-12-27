package domain.automationpractice.interactions;

import domain.automationpractice.pom.models.AutomationPracticeHomePage;
import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;

public class Search {

  public static Interaction forText(String text) {
    return actor ->
        BrowseTheWeb.as(actor)
            .onPage(AutomationPracticeHomePage.class)
            .enterIntoSearchInput(text)
            .clickSearchButton();
  }
}
