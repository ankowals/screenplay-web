package screenplay.saucedemo.questions;

import framework.screenplay.Question;
import framework.web.screenplay.BrowseTheWeb;
import pom.saucedemo.ProductsPage;

public class ThePage {
  public static Question<String> title() {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductsPage.class).getTitle();
  }
}
