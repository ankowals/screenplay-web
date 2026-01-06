package domain.formio.interactions;

import domain.BrowseTheWeb;
import domain.formio.pom.AngularFormExamplePage;
import framework.screenplay.Interaction;

public class Open {

  public static Interaction browser() {
    return actor -> BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).open();
  }
}
