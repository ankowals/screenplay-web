package com.github.ankowals.domain.saucedemo.interactions;

import com.github.ankowals.abilities.BrowseTheWeb;
import com.github.ankowals.domain.saucedemo.pom.CartPage;
import com.github.ankowals.domain.saucedemo.pom.ProductsPage;
import com.github.ankowals.framework.screenplay.Interaction;

public class GoTo {
  public static Interaction cart() {
    return actor -> BrowseTheWeb.as(actor).on(ProductsPage::new).clickCartButton();
  }

  public static Interaction checkout() {
    return actor -> BrowseTheWeb.as(actor).on(CartPage::new).clickCheckout();
  }
}
