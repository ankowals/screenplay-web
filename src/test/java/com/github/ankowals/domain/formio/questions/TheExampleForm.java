package com.github.ankowals.domain.formio.questions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.formio.pom.AngularFormExamplePage;
import com.github.ankowals.framework.screenplay.Question;

public class TheExampleForm {

  public static Question<String> submitMessage() {
    return actor -> BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).getSubmitMessage();
  }
}
