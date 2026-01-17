package com.github.ankowals.domain.saucedemo.questions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.saucedemo.pom.ProductsPage;
import com.github.ankowals.framework.screenplay.Question;

public class ThePage {
  public static Question<String> title() {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductsPage.class).getTitle();
  }
}
