package screenplay.saucedemo;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import pom.saucedemo.ProductsPage;

public class Add {
  public static Interaction toCart(String product) {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductsPage.class).clickAddToCart(product);
  }
}
