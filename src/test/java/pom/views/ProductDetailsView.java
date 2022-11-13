package pom.views;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import framework.pom.elements.Element;
import framework.pom.elements.ElementImpl;
import framework.pom.page.BaseView;

import java.util.List;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElementsLocatedBy;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class ProductDetailsView extends BaseView {

    private static final By PRICE_TEXT = By.xpath("//*[@id='our_price_display']");
    private static final By OLD_PRICE_TEXT = By.xpath("//*[@id='old_price_display']");
    private static final By REDUCTION_PERCENT_TEXT = By.xpath("//*[@id='reduction_percent_display']");
    private static final By REFERENCE_TEXT = By.xpath("//*[@id='product_reference']");
    private static final By CONDITION_TEXT = By.xpath("//*[@id='product_condition']");
    private static final By SHORT_DESCRIPTION_TEXT = By.xpath("//*[@id='short_description_content']/p");
    private static final By AVAILABLE_COLOR = By.xpath("//*[@id='color_to_pick_list']/li");
    private static final By DATA_SHEET_TABLE = By.xpath("//table[@class='table-data-sheet']");

    public ProductDetailsView(WebDriver driver) {
        super(driver);
    }

    public Element getPriceElement() {
        WebElement element = wait.until(visibilityOfElementLocated(PRICE_TEXT));
        return ElementImpl.of(element);
    }

    public Element getOldPriceElement() {
        WebElement element = wait.until(visibilityOfElementLocated(OLD_PRICE_TEXT));
        return ElementImpl.of(element);
    }

    public Element getReductionPercentElement() {
        WebElement element = wait.until(visibilityOfElementLocated(REDUCTION_PERCENT_TEXT));
        return ElementImpl.of(element);
    }

    public Element getReferenceElement() {
        WebElement element = wait.until(visibilityOfElementLocated(REFERENCE_TEXT));
        return ElementImpl.of(element);
    }

    public Element getConditionElement() {
        WebElement element = wait.until(visibilityOfElementLocated(CONDITION_TEXT));
        return ElementImpl.of(element);
    }

    public Element getShortDescriptionElement() {
        WebElement element = wait.until(visibilityOfElementLocated(SHORT_DESCRIPTION_TEXT));
        return ElementImpl.of(element);
    }

    public List<Element> getAvailableColorElements() {
        List<WebElement> elements = wait.until(visibilityOfAllElementsLocatedBy(AVAILABLE_COLOR));
        return elements.stream()
                .map(ElementImpl::of)
                .collect(Collectors.toList());
    }

    public ElementImpl getDataSheetTable() {
        WebElement element = wait.until(visibilityOfElementLocated(DATA_SHEET_TABLE));
        return ElementImpl.of(element);
    }
}
