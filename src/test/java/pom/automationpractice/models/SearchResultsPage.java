package pom.automationpractice.models;

import framework.web.pom.page.BasePage;
import org.openqa.selenium.WebDriver;
import pom.automationpractice.views.SearchResultsView;

public class SearchResultsPage extends BasePage {

  private final SearchResultsView view = new SearchResultsView(this.driver);

  public SearchResultsPage(WebDriver driver) {
    super(driver);
  }

  public ProductDetailsPage clickProductLinkButton(String product) {
    this.view.getProductLink(product).click();
    return new ProductDetailsPage(this.driver);
  }
}
