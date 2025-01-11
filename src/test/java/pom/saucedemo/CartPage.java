package pom.saucedemo;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import framework.web.pom.elements.Button;
import framework.web.pom.elements.Element;
import framework.web.pom.elements.common.ButtonImpl;
import framework.web.pom.elements.common.ElementImpl;
import framework.web.pom.page.BasePage;
import framework.web.pom.page.BaseView;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class CartPage extends BasePage {

  CartView view = new CartView(this.driver);
  HeaderView headerView = new HeaderView(this.driver);

  public CartPage(WebDriver driver) {
    super(driver);
  }

  @Override
  public String getTitle() {
    return this.headerView.titleElement().getText();
  }

  public CheckoutYourInformationPage clickCheckout() {
    this.view.checkoutButton().click();
    return new CheckoutYourInformationPage(this.driver);
  }

  public List<CartItem> getCartItems() {
    String source = this.view.cartListElement().getWebElementSource();

    List<CartItem> items = new ArrayList<>();

    Jsoup.parseBodyFragment(source)
        .select(".cart_item")
        .forEach(
            row -> {
              CartItem actual =
                  CartItem.builder()
                      .quantity(Integer.parseInt(row.select(".cart_quantity").text()))
                      .name(row.select(".inventory_item_name").text())
                      .description(row.select(".inventory_item_desc").text())
                      .price(row.select(".inventory_item_price").text())
                      .build();

              items.add(actual);
            });

    return items;
  }

  static class CartView extends BaseView {

    CartView(WebDriver driver) {
      super(driver);
    }

    Button checkoutButton() {
      return ButtonImpl.of(
          this.wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout"))));
    }

    Element cartListElement() {
      WebElement element =
          this.wait.until(visibilityOfElementLocated(By.xpath("//*[@data-test='cart-list']")));
      return ElementImpl.of(element);
    }
  }

  @Builder
  public record CartItem(String name, int quantity, String description, String price) {}
}
