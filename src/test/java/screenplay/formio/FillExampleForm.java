package screenplay.formio;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import pom.formio.AngularFormExamplePage;

public class FillExampleForm {
  public static Interaction firstName(String firstName) {
    return actor ->
        BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).enterFirstName(firstName);
  }
}
