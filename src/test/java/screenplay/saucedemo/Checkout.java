package screenplay.saucedemo;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import java.util.function.Consumer;
import pom.saucedemo.CheckoutYourInformationPage;
import pom.saucedemo.ProductsPage;

public class Checkout {
  public static Interaction as(Consumer<CheckoutYourInformationPage> checkoutFormData) {
    return actor -> {
      CheckoutYourInformationPage checkoutYourInformationPage =
          BrowseTheWeb.as(actor).onPage(ProductsPage.class).clickCartButton().clickCheckout();
      checkoutFormData.accept(checkoutYourInformationPage);
    };
  }
}
