package screenplay.automationpractice.questions;

import pom.automationpractice.models.ProductDetailsPage;
import framework.web.screenplay.BrowseTheWeb;
import framework.screenplay.Question;

import java.util.List;
import java.util.Map;

public class ProductDetails {

    public static Question<testdata.ProductDetails> get() {
        return actor -> ProductDetails.doGet(BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class));
    }

    public static Question<String> price() {
        return actor -> BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class).getPrice();
    }

    public static Question<String> shortDescription() {
        return actor -> BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class).getShortDescription();
    }

    public static Question<List<Map<String, String>>> dataSheet() {
        return actor -> BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class).getDataSheet();
    }

    private static testdata.ProductDetails doGet(ProductDetailsPage page) {
        return new testdata.ProductDetails.Builder()
                .with($ -> {
                    $.price = page.getPrice();
                    $.shortDescription = page.getShortDescription();
                }).create();
    }
}
