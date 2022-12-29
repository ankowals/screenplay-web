package pom.parasoft.views;

import framework.web.pom.elements.ElementImpl;
import framework.web.pom.page.BaseView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class ParasoftProductsView extends BaseView {

    public ParasoftProductsView(WebDriver driver) {
        super(driver);
    }

    public ElementImpl getTitle() {
        WebElement element = wait.until(visibilityOfElementLocated(By.xpath("//*[@class='inner-banner-cont']/span")));
        return ElementImpl.of(element);
    }

}
