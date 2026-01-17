package com.github.ankowals.domain.automationpractice.questions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.framework.screenplay.Question;
import com.github.ankowals.framework.web.pom.page.BasePage;

public class ThePage {
  public static <T extends BasePage> Question<String> title(Class<T> page) {
    return actor -> BrowseTheWeb.as(actor).onPage(page).getTitle();
  }
}
