package screenplay.formio;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import pom.formio.AngularFormExamplePage;

public class Submit {
  public static Interaction exampleForm() {
    return actor -> BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).clickSubmit();
  }
}
