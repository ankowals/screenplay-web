package pom.automationpractice.views;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

import framework.web.pom.elements.*;
import framework.web.pom.elements.common.ButtonImpl;
import framework.web.pom.page.BaseView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchResultsView extends BaseView {

  private static By createProductLinkSelector(String product) {
    return By.xpath("(//a[@title='" + product + "' and ancestor::div[@class='right-block']])[1]");
  }

  public SearchResultsView(WebDriver driver) {
    super(driver);
  }

  public Button productLink(String product) {
    WebElement element = this.wait.until(elementToBeClickable(createProductLinkSelector(product)));
    return ButtonImpl.of(element);
  }
}
