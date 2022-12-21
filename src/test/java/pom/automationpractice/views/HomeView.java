package pom.automationpractice.views;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import framework.pom.elements.*;
import framework.pom.page.BaseView;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class HomeView extends BaseView {

    private static final By SIGN_IN_BUTTON = By.xpath("//a[@class='login']");
    private static final By SEARCH_BUTTON = By.xpath("//button[@name='submit_search']");

    private static final By SEARCH_INPUT = By.xpath("//input[@id='search_query_top']");

    public HomeView(WebDriver driver) {
        super(driver);
    }

    public Button getSignInButton() {
        WebElement element = wait.until(elementToBeClickable(SIGN_IN_BUTTON));
        return ButtonImpl.of(element);
    }

    public Button getSearchButton() {
        WebElement element = wait.until(elementToBeClickable(SEARCH_BUTTON));
        return ButtonImpl.of(element);
    }

    public Input getSearchInput() {
        WebElement element = wait.until(visibilityOfElementLocated(SEARCH_INPUT));
        return InputImpl.of(element);
    }
}
