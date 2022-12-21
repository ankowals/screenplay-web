package screenplay.automationpractice.questions;

import pom.automationpractice.models.ProductDetailsPage;
import screenplay.abilities.BrowseTheWeb;
import framework.screenplay.Question;

import java.util.List;
import java.util.Map;

public class ProductDetails {

    public static Question<testdata.ProductDetails> getDetails() {
        return actor -> doGetDetails(BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class));
    }

    public static Question<String> getPrice() {
        return actor -> BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class).getPrice();
    }

    public static Question<String> getShortDescription() {
        return actor -> BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class).getShortDescription();
    }

    public static Question<List<Map<String, String>>> getDataSheet() {
        return actor -> BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class).getDataSheet();
    }

    private static testdata.ProductDetails doGetDetails(ProductDetailsPage page) {
        return new testdata.ProductDetails.Builder()
                .with($ -> {
                    $.price = page.getPrice();
                    $.shortDescription = page.getShortDescription();
                }).create();
    }
}
