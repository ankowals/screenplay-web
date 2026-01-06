package domain.automationpractice.questions;

import domain.BrowseTheWeb;
import domain.automationpractice.model.Product;
import domain.automationpractice.pom.models.ProductDetailsPage;
import framework.screenplay.Question;
import java.util.List;
import java.util.Map;

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
