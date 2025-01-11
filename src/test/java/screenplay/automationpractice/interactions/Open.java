package screenplay;

import framework.screenplay.Interaction;
import framework.web.pom.page.BasePage;
import framework.web.screenplay.BrowseTheWeb;

public class Open {

  public static Interaction browser(String url, Class<? extends BasePage> page) {
    return actor -> BrowseTheWeb.as(actor).onPage(page).open(url);
  }
}
