package domain.saucedemo.interactions;

import domain.saucedemo.pom.ProductsPage;
import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;

public class Add {
  public static Interaction toCart(String product) {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductsPage.class).clickAddToCart(product);
  }
}
