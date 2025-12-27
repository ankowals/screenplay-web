package domain.automationpractice.questions;

import framework.screenplay.Question;
import framework.web.pom.page.BasePage;
import framework.web.screenplay.BrowseTheWeb;

public class ThePage {
  public static <T extends BasePage> Question<String> title(Class<T> page) {
    return actor -> BrowseTheWeb.as(actor).onPage(page).getTitle();
  }
}
