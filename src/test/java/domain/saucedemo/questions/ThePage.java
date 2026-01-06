package domain.saucedemo.questions;

import domain.BrowseTheWeb;
import domain.saucedemo.pom.ProductsPage;
import framework.screenplay.Question;

public class ThePage {
  public static Question<String> title() {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductsPage.class).getTitle();
  }
}
