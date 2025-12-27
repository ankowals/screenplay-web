package domain.formio.questions;

import domain.formio.pom.AngularFormExamplePage;
import framework.screenplay.Question;
import framework.web.screenplay.BrowseTheWeb;

public class TheExampleForm {

  public static Question<String> submitMessage() {
    return actor -> BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).getSubmitMessage();
  }
}
