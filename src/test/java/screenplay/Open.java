package screenplay;

import framework.screenplay.Interaction;
import framework.screenplay.actor.Actor;
import framework.web.pom.page.BasePage;
import framework.web.screenplay.BrowseTheWeb;

public class Open {

  public static Interaction<Actor> browser(String url) {
    return actor -> BrowseTheWeb.as(actor).onPage(PageUrl.getMapping(url)).open(url);
  }

  public static Interaction<Actor> browser(String url, Class<? extends BasePage> page) {
    return actor -> BrowseTheWeb.as(actor).onPage(page).open(url);
  }
}
