package screenplay.automationpractice.questions;

import framework.screenplay.Question;
import framework.screenplay.actor.Actor;
import framework.web.screenplay.BrowseTheWeb;
import java.util.List;
import java.util.Map;
import pom.automationpractice.models.ProductDetailsPage;

public class ProductDetails {

  public static Question<testdata.ProductDetails, Actor> get() {
    return actor -> ProductDetails.doGet(BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class));
  }

  public static Question<String, Actor> price() {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class).getPrice();
  }

  public static Question<String, Actor> shortDescription() {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class).getShortDescription();
  }

  public static Question<List<Map<String, String>>, Actor> dataSheet() {
    return actor -> BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class).getDataSheet();
  }

  private static testdata.ProductDetails doGet(ProductDetailsPage page) {
    return new testdata.ProductDetails.Builder()
        .with(
            $ -> {
              $.price = page.getPrice();
              $.shortDescription = page.getShortDescription();
            })
        .create();
  }
}
