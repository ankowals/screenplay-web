package com.github.ankowals.domain.automationpractice.pom.models;

import com.github.ankowals.domain.automationpractice.pom.views.HomeView;
import com.github.ankowals.framework.web.pom.page.BasePage;
import org.openqa.selenium.WebDriver;

public class AutomationPracticeHomePage extends BasePage {

  private final HomeView view = new HomeView(this.driver);

  public AutomationPracticeHomePage(WebDriver driver) {
    super(driver);
  }

  public AutomationPracticeHomePage open() {
    this.open("http://automationpractice.com/index.php");
    return this;
  }

  public AuthenticationPage clickSignInButton() {
    this.view.signInButton().click();
    return new AuthenticationPage(this.driver);
  }

  public AutomationPracticeHomePage enterIntoSearchInput(String text) {
    this.view.searchInput().insert(text);
    return this;
  }

  public SearchResultsPage clickSearchButton() {
    this.view.searchButton().click();
    return new SearchResultsPage(this.driver);
  }
}
