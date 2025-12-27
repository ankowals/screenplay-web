package domain.formio.interactions;

import domain.formio.pom.AngularFormExamplePage;
import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;

public class Open {

  public static Interaction browser() {
    return actor -> BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).open();
  }
}
