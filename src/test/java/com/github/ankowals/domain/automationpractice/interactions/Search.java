package com.github.ankowals.domain.automationpractice.interactions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.automationpractice.pom.models.AutomationPracticeHomePage;
import com.github.ankowals.framework.screenplay.Interaction;

public class Search {

  public static Interaction forText(String text) {
    return actor ->
        BrowseTheWeb.as(actor)
            .onPage(AutomationPracticeHomePage.class)
            .enterIntoSearchInput(text)
            .clickSearchButton();
  }
}
