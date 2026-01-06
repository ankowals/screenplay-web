package domain.saucedemo.questions;

import domain.BrowseTheWeb;
import domain.saucedemo.interactions.GoTo;
import domain.saucedemo.pom.CartPage;
import framework.screenplay.Question;

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
