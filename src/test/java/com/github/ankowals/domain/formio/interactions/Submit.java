package com.github.ankowals.domain.formio.interactions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.formio.pom.AngularFormExamplePage;
import com.github.ankowals.framework.screenplay.Interaction;

public class Submit {
  public static Interaction exampleForm() {
    return actor -> BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).clickSubmit();
  }
}
