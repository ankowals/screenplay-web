package pom.automationpractice.models;

import framework.web.pom.page.BasePage;
import org.openqa.selenium.WebDriver;
import pom.automationpractice.views.HomeView;

public class HomePage extends BasePage {

  private final HomeView view = new HomeView(this.driver);

  public HomePage(WebDriver driver) {
    super(driver);
  }

  public AuthenticationPage clickSignInButton() {
    this.view.getSignInButton().click();
    return new AuthenticationPage(this.driver);
  }

  public HomePage enterIntoSearchInput(String text) {
    this.view.getSearchInput().insert(text);
    return this;
  }

  public SearchResultsPage clickSearchButton() {
    this.view.getSearchButton().click();
    return new SearchResultsPage(this.driver);
  }
}
