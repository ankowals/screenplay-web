package screenplay.saucedemo;

import framework.screenplay.Question;
import framework.web.screenplay.BrowseTheWeb;
import pom.saucedemo.CartPage;

public class TheItem {
  public static Question<CartPage.CartItem> inCart(String name) {
    return actor -> {
      actor.attemptsTo(GoTo.cart());
      return BrowseTheWeb.as(actor).onPage(CartPage.class).getCartItems().stream()
          .filter(item -> item.name().equals(name))
          .findFirst()
          .orElseThrow();
    };
  }
}
