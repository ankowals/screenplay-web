package screenplay.formio.interactions;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import pom.formio.AngularFormExamplePage;

public class Open {

  public static Interaction browser() {
    return actor -> BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).open();
  }
}
