package screenplay.saucedemo;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import pom.saucedemo.CartPage;
import pom.saucedemo.ProductsPage;

public class GoTo {
  public static Interaction cart() {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductsPage.class).clickCartButton();
  }

  public static Interaction checkout() {
    return actor -> BrowseTheWeb.as(actor).onPage(CartPage.class).clickCheckout();
  }
}
