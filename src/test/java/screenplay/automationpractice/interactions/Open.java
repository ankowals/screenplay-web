package screenplay.automationpractice.interactions;

import framework.screenplay.Interaction;
import framework.web.pom.page.BasePage;
import framework.web.screenplay.BrowseTheWeb;
import pom.automationpractice.models.AutomationPracticeHomePage;

public class Open {

  public static Interaction browser() {
    return actor -> BrowseTheWeb.as(actor).onPage(AutomationPracticeHomePage.class).open();
  }

  public static Interaction browser(String url, Class<? extends BasePage> clazz) {
    return actor -> BrowseTheWeb.as(actor).onPage(clazz).open(url);
  }
}
