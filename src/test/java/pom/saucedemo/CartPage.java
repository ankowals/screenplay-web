package pom.saucedemo;

import framework.web.pom.elements.Button;
import framework.web.pom.elements.common.ButtonImpl;
import framework.web.pom.page.BasePage;
import framework.web.pom.page.BaseView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {

  CartView view = new CartView(this.driver);
  HeaderView headerView = new HeaderView(this.driver);

  public CartPage(WebDriver driver) {
    super(driver);
  }

  @Override
  public String getTitle() {
    return this.headerView.getTitleElement().getText();
  }

  public CheckoutYourInformationPage clickCheckout() {
    this.view.getCheckoutButton().click();
    return new CheckoutYourInformationPage(this.driver);
  }

  static class CartView extends BaseView {

    CartView(WebDriver driver) {
      super(driver);
    }

    Button getCheckoutButton() {
      return ButtonImpl.of(
          this.wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))));
    }
  }
}
