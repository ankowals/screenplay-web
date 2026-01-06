package domain.formio.interactions;

import domain.BrowseTheWeb;
import domain.formio.pom.AngularFormExamplePage;
import framework.screenplay.Interaction;

public class Submit {
  public static Interaction exampleForm() {
    return actor -> BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).clickSubmit();
  }
}
