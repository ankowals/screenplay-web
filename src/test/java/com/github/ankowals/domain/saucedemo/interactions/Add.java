package com.github.ankowals.domain.saucedemo.interactions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.saucedemo.pom.ProductsPage;
import com.github.ankowals.framework.screenplay.Interaction;

public class Add {
  public static Interaction toCart(String product) {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductsPage.class).clickAddToCart(product);
  }
}
