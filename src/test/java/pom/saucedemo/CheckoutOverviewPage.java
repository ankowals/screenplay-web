package pom.saucedemo;

import framework.web.pom.elements.Button;
import framework.web.pom.elements.Element;
import framework.web.pom.elements.common.ButtonImpl;
import framework.web.pom.elements.common.ElementImpl;
import framework.web.pom.page.BasePage;
import framework.web.pom.page.BaseView;
import lombok.Builder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CheckoutOverviewPage extends BasePage {

  HeaderView headerView = new HeaderView(this.driver);
  CheckoutOverviewView view = new CheckoutOverviewView(this.driver);

  public CheckoutOverviewPage(WebDriver driver) {
    super(driver);
  }

  @Override
  public String getTitle() {
    return this.headerView.titleElement().getText();
  }

  public ProductsPage clickCancel() {
    this.view.cancelButton().click();
    return new ProductsPage(this.driver);
  }

  public SummaryInfo getSummaryInfo() {
    return SummaryInfo.builder()
        .paymentInfo(this.view.valueElement("payment").getText())
        .shippingInfo(this.view.valueElement("shipping").getText())
        .priceTotal(
            PriceTotal.builder()
                .itemTotal(this.view.itemTotalElement().getText().split(":")[1].trim())
                .tax(this.view.taxElement().getText().split(":")[1].trim())
                .total(this.view.totalElement().getText().split(":")[1].trim())
                .build())
        .build();
  }

  static class CheckoutOverviewView extends BaseView {

    CheckoutOverviewView(WebDriver driver) {
      super(driver);
    }

    Button finishButton() {
      return ButtonImpl.of(
          this.wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))));
    }

    Button cancelButton() {
      return ButtonImpl.of(
          this.wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))));
    }

    Element valueElement(String prefix) {
      return ElementImpl.of(
          this.wait.until(
              ExpectedConditions.elementToBeClickable(
                  By.xpath(String.format("//*[@data-test='%s-info-value']", prefix)))));
    }

    Element taxElement() {
      return ElementImpl.of(
          this.wait.until(
              ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-test='tax-label']"))));
    }

    Element totalElement() {
      return ElementImpl.of(
          this.wait.until(
              ExpectedConditions.elementToBeClickable(By.xpath("//*[@data-test='total-label']"))));
    }

    Element itemTotalElement() {
      return ElementImpl.of(
          this.wait.until(
              ExpectedConditions.elementToBeClickable(
                  By.xpath("//*[@data-test='subtotal-label']"))));
    }
  }

  @Builder
  public record SummaryInfo(String paymentInfo, String shippingInfo, PriceTotal priceTotal) {}

  @Builder
  public record PriceTotal(String itemTotal, String tax, String total) {}
}
