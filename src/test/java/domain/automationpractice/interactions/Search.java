package domain.automationpractice.interactions;

import domain.BrowseTheWeb;
import domain.automationpractice.pom.models.AutomationPracticeHomePage;
import framework.screenplay.Interaction;

public class Search {

  public static Interaction forText(String text) {
    return actor ->
        BrowseTheWeb.as(actor)
            .onPage(AutomationPracticeHomePage.class)
            .enterIntoSearchInput(text)
            .clickSearchButton();
  }
}
