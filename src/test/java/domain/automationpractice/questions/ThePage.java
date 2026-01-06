package domain.automationpractice.questions;

import domain.BrowseTheWeb;
import framework.screenplay.Question;
import framework.web.pom.page.BasePage;

public class ThePage {
  public static <T extends BasePage> Question<String> title(Class<T> page) {
    return actor -> BrowseTheWeb.as(actor).onPage(page).getTitle();
  }
}
