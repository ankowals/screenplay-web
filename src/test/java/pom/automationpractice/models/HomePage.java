package pom.automationpractice.models;

import org.openqa.selenium.WebDriver;
import framework.pom.page.BasePage;
import pom.automationpractice.views.HomeView;

public class HomePage extends BasePage {

    private final HomeView view = new HomeView(driver);

    public HomePage(WebDriver driver) { super(driver); }

    public AuthenticationPage clickSignInButton() {
        view.getSignInButton().click();
        return new AuthenticationPage(driver);
    }

    public HomePage enterIntoSearchInput(String text) {
        view.getSearchInput().insert(text);
        return this;
    }

    public SearchResultsPage clickSearchButton() {
        view.getSearchButton().click();
        return new SearchResultsPage(driver);
    }
}
