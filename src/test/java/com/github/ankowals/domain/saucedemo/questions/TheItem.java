package com.github.ankowals.domain.saucedemo.questions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.saucedemo.interactions.GoTo;
import com.github.ankowals.domain.saucedemo.pom.CartPage;
import com.github.ankowals.framework.screenplay.Question;

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
