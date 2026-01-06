package domain.automationpractice.interactions;

import domain.BrowseTheWeb;
import domain.automationpractice.pom.models.AutomationPracticeHomePage;
import framework.screenplay.Interaction;
import framework.web.pom.page.BasePage;

public class Open {

  public static Interaction automationPractice() {
    return actor -> BrowseTheWeb.as(actor).onPage(AutomationPracticeHomePage.class).open();
  }

  public static Interaction browser(String url, Class<? extends BasePage> clazz) {
    return actor -> BrowseTheWeb.as(actor).onPage(clazz).open(url);
  }
}
