package pom.views;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pom.framework.elements.*;
import pom.framework.page.BaseView;

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
