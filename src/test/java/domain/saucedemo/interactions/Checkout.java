package domain.saucedemo.interactions;

import domain.BrowseTheWeb;
import domain.saucedemo.pom.CheckoutYourInformationPage;
import framework.screenplay.Interaction;
import java.util.function.Consumer;

public class Checkout {
  public static Interaction completingForm(Consumer<CheckoutYourInformationPage> checkoutFormData) {
    return actor -> {
      actor.attemptsTo(GoTo.checkout());
      checkoutFormData.accept(BrowseTheWeb.as(actor).onPage(CheckoutYourInformationPage.class));
    };
  }
}
