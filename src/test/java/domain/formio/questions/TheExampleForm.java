package domain.formio.questions;

import domain.BrowseTheWeb;
import domain.formio.pom.AngularFormExamplePage;
import framework.screenplay.Question;

public class TheExampleForm {

  public static Question<String> submitMessage() {
    return actor -> BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).getSubmitMessage();
  }
}
