package screenplay.formio.interactions;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import java.util.function.Consumer;
import pom.formio.AngularFormExamplePage;

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
