package domain.saucedemo.interactions;

import domain.BrowseTheWeb;
import domain.saucedemo.pom.CartPage;
import domain.saucedemo.pom.ProductsPage;
import framework.screenplay.Interaction;

public class GoTo {
  public static Interaction cart() {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductsPage.class).clickCartButton();
  }

  public static Interaction checkout() {
    return actor -> BrowseTheWeb.as(actor).onPage(CartPage.class).clickCheckout();
  }
}
