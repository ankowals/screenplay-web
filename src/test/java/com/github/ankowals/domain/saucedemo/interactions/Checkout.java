package com.github.ankowals.domain.saucedemo.interactions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.saucedemo.pom.CheckoutYourInformationPage;
import com.github.ankowals.framework.screenplay.Interaction;
import java.util.function.Consumer;

public class Checkout {
  public static Interaction completingForm(Consumer<CheckoutYourInformationPage> checkoutFormData) {
    return actor -> {
      actor.attemptsTo(GoTo.checkout());
      checkoutFormData.accept(BrowseTheWeb.as(actor).onPage(CheckoutYourInformationPage.class));
    };
  }
}
