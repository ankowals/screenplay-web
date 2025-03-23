package screenplay.formio.questions;

import framework.screenplay.Question;
import framework.web.screenplay.BrowseTheWeb;
import pom.formio.AngularFormExamplePage;

public class TheExampleForm {

  public static Question<String> submitMessage() {
    return actor -> BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).getSubmitMessage();
  }
}
