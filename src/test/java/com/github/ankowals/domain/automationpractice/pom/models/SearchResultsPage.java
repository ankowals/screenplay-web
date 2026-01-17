package com.github.ankowals.domain.automationpractice.pom.models;

import com.github.ankowals.domain.automationpractice.pom.views.SearchResultsView;
import com.github.ankowals.framework.web.pom.page.BasePage;
import org.openqa.selenium.WebDriver;

public class SearchResultsPage extends BasePage {

  private final SearchResultsView view = new SearchResultsView(this.driver);

  public SearchResultsPage(WebDriver driver) {
    super(driver);
  }

  public ProductDetailsPage clickProductLinkButton(String product) {
    this.view.productLink(product).click();
    return new ProductDetailsPage(this.driver);
  }
}
