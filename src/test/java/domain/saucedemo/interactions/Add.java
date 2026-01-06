package domain.saucedemo.interactions;

import domain.BrowseTheWeb;
import domain.saucedemo.pom.ProductsPage;
import framework.screenplay.Interaction;

public class Add {
  public static Interaction toCart(String product) {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductsPage.class).clickAddToCart(product);
  }
}
