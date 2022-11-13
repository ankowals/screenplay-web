package pom.models;

import org.openqa.selenium.WebDriver;
import framework.pom.page.BasePage;
import pom.views.SearchResultsView;

public class SearchResultsPage extends BasePage {

    private final SearchResultsView view = new SearchResultsView(driver);

    public SearchResultsPage(WebDriver driver) { super(driver); }

    public ProductDetailsPage clickProductLinkButton(String product) {
        view.getProductLink(product).click();
        return new ProductDetailsPage(driver);
    }
}
