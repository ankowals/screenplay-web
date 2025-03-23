package screenplay.saucedemo.interactions;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import java.util.function.Consumer;
import pom.saucedemo.CheckoutYourInformationPage;

public class Checkout {
  public static Interaction as(Consumer<CheckoutYourInformationPage> checkoutFormData) {
    return actor -> {
      actor.attemptsTo(GoTo.checkout());
      checkoutFormData.accept(BrowseTheWeb.as(actor).onPage(CheckoutYourInformationPage.class));
    };
  }
}
