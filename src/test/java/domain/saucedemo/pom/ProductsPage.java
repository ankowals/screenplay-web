package domain.saucedemo.pom;

import framework.web.pom.elements.Button;
import framework.web.pom.elements.common.ButtonImpl;
import framework.web.pom.page.BasePage;
import framework.web.pom.page.BaseView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductsPage extends BasePage {

  HeaderView headerView = new HeaderView(this.driver);
  ProductsView view = new ProductsView(this.driver);

  public ProductsPage(WebDriver driver) {
    super(driver);
  }

  @Override
  public String getTitle() {
    return this.headerView.titleElement().getText();
  }

  public CartPage clickCartButton() {
    this.headerView.cartButton().click();
    return new CartPage(this.driver);
  }

  public ProductsPage clickAddToCart(String product) {
    this.view.getAddToCartButton(product).click();
    return this;
  }

  static class ProductsView extends BaseView {

    ProductsView(WebDriver driver) {
      super(driver);
    }

    Button getAddToCartButton(String product) {
      String suffix = product.toLowerCase().replaceAll(" ", "-");
      return ButtonImpl.of(
          this.wait.until(
              ExpectedConditions.elementToBeClickable(
                  By.id(String.format("add-to-cart-%s", suffix)))));
    }
  }
}
