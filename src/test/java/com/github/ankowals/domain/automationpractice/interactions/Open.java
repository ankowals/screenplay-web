package com.github.ankowals.domain.automationpractice.interactions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.automationpractice.pom.models.AutomationPracticeHomePage;
import com.github.ankowals.framework.screenplay.Interaction;
import com.github.ankowals.framework.web.pom.page.BasePage;

public class Open {

  public static Interaction automationPractice() {
    return actor -> BrowseTheWeb.as(actor).onPage(AutomationPracticeHomePage.class).open();
  }

  public static Interaction browser(String url, Class<? extends BasePage> clazz) {
    return actor -> BrowseTheWeb.as(actor).onPage(clazz).open(url);
  }
}
