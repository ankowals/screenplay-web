package screenplay.questions;

import io.qameta.allure.Step;
import pom.models.ProductDetailsPage;
import screenplay.abilities.BrowseTheWeb;
import screenplay.framework.Question;

import java.util.List;
import java.util.Map;

public class ProductDetails {

    @Step("Get product details")
    public static Question<testdata.ProductDetails> getDetails() {
        return actor -> doGetDetails(BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class));
    }

    @Step("Get product price")
    public static Question<String> getPrice() {
        return actor -> BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class).getPrice();
    }

    @Step("Get short description")
    public static Question<String> getShortDescription() {
        return actor -> BrowseTheWeb.as(actor).onPage(ProductDetailsPage.class).getShortDescription();
    }

    @Step("Get data sheet")
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
