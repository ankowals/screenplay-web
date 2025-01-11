package screenplay.automationpractice.questions;

import framework.screenplay.Question;
import framework.web.screenplay.BrowseTheWeb;
import java.util.List;
import java.util.Map;
import pom.automationpractice.models.ProductDetailsPage;
import screenplay.automationpractice.testdata.Product;

public class TheProduct {

  public static Question<Product> details() {
    return actor -> {
      ProductDetailsPage productDetailsPage =
          BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class);
      return new Product.Builder()
          .with(
              $ -> {
                $.price = productDetailsPage.getPrice();
                $.shortDescription = productDetailsPage.getShortDescription();
              })
          .build();
    };
  }

  public static Question<List<Map<String, String>>> dataSheet() {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class).getDataSheet();
  }
}
