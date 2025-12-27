package domain.saucedemo.questions;

import domain.saucedemo.pom.ProductsPage;
import framework.screenplay.Question;
import framework.web.screenplay.BrowseTheWeb;

public class ThePage {
  public static Question<String> title() {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductsPage.class).getTitle();
  }
}
