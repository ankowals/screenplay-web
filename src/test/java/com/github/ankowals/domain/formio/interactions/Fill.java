package com.github.ankowals.domain.formio.interactions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.formio.pom.AngularFormExamplePage;
import com.github.ankowals.framework.screenplay.Interaction;
import java.util.function.Consumer;

public class Fill {

  public static FillExampleFormFactory exampleForm() {
    return new FillExampleFormFactory();
  }

  // better alternative
  public static Interaction exampleForm(Consumer<AngularFormExamplePage> customizer) {
    return actor -> customizer.accept(BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class));
  }

  public static class FillExampleFormFactory {
    public Interaction firstName(String firstName) {
      return actor ->
          BrowseTheWeb.as(actor).onPage(AngularFormExamplePage.class).enterFirstName(firstName);
    }
  }
}
