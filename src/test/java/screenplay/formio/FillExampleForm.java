package screenplay.formio;

import framework.screenplay.Interaction;
import framework.screenplay.actor.Actor;
import framework.web.screenplay.BrowseTheWeb;
import pom.formio.AngularFormExamplePage;

public class FillExampleForm {
  public static Interaction<Actor> firstName(String firstName) {
    return actor ->
        BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).enterFirstName(firstName);
  }
}
