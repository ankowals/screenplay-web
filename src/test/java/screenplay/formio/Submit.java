package screenplay.formio;

import framework.screenplay.Interaction;
import framework.screenplay.actor.Actor;
import framework.web.screenplay.BrowseTheWeb;
import pom.formio.AngularFormExamplePage;

public class Submit {
  public static Interaction<Actor> exampleForm() {
    return actor -> BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).clickSubmit();
  }
}
