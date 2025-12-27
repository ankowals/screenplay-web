package domain.formio.interactions;

import domain.formio.pom.AngularFormExamplePage;
import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;

public class Submit {
  public static Interaction exampleForm() {
    return actor -> BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).clickSubmit();
  }
}
