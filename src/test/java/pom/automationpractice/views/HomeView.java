package pom.automationpractice.views;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import framework.web.pom.elements.*;
import framework.web.pom.page.BaseView;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class HomeView extends BaseView {

    public HomeView(WebDriver driver) {
        super(driver);
    }

    public Button getSignInButton() {
        WebElement element = this.wait.until(elementToBeClickable(By.xpath("//a[@class='login']")));
        return ButtonImpl.of(element);
    }

    public Button getSearchButton() {
        WebElement element = this.wait.until(elementToBeClickable(By.xpath("//button[@name='submit_search']")));
        return ButtonImpl.of(element);
    }

    public Input getSearchInput() {
        WebElement element = this.wait.until(visibilityOfElementLocated(By.xpath("//input[@id='search_query_top']")));
        return InputImpl.of(element);
    }
}
