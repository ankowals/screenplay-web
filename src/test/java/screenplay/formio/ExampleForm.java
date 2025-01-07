package screenplay.formio;

import framework.screenplay.Question;
import framework.screenplay.actor.Actor;
import framework.web.screenplay.BrowseTheWeb;
import pom.formio.AngularFormExamplePage;

public class ExampleForm {

  public static Question<String, Actor> submitMessage() {
    return actor ->
        BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).getSubmitMessageText();
  }
}
