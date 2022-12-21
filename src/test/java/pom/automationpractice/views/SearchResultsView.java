package pom.automationpractice.views;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import framework.pom.elements.*;
import framework.pom.page.BaseView;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class SearchResultsView extends BaseView {

    private static By createProductLinkSelector(String product) {
        return By.xpath("(//a[@title='" + product + "' and ancestor::div[@class='right-block']])[1]");
    }

    public SearchResultsView(WebDriver driver) {
        super(driver);
    }

    public Button getProductLink(String product) {
        WebElement element = wait.until(elementToBeClickable(createProductLinkSelector(product)));
        return ButtonImpl.of(element);
    }
}
