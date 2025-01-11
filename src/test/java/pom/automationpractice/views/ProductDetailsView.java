package pom.automationpractice.views;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import framework.web.pom.elements.Element;
import framework.web.pom.elements.common.ElementImpl;
import framework.web.pom.page.BaseView;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProductDetailsView extends BaseView {

  public ProductDetailsView(WebDriver driver) {
    super(driver);
  }

  public Element priceElement() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//*[@id='our_price_display']")));
    return ElementImpl.of(element);
  }

  public Element oldPriceElement() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//*[@id='old_price_display']")));
    return ElementImpl.of(element);
  }

  public Element reductionPercentElement() {
    WebElement element =
        this.wait.until(
            visibilityOfElementLocated(By.xpath("//*[@id='reduction_percent_display']")));
    return ElementImpl.of(element);
  }

  public Element referenceElement() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//*[@id='product_reference']")));
    return ElementImpl.of(element);
  }

  public Element conditionElement() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//*[@id='product_condition']")));
    return ElementImpl.of(element);
  }

  public Element shortDescriptionElement() {
    WebElement element =
        this.wait.until(
            visibilityOfElementLocated(By.xpath("//*[@id='short_description_content']/p")));
    return ElementImpl.of(element);
  }

  public List<Element> availableColorElements() {
    List<WebElement> elements =
        this.wait.until(
            visibilityOfAllElementsLocatedBy(By.xpath("//*[@id='color_to_pick_list']/li")));
    return elements.stream().map(ElementImpl::of).collect(Collectors.toList());
  }

  public Element dataSheetTable() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//table[@class='table-data-sheet']")));
    return ElementImpl.of(element);
  }
}
