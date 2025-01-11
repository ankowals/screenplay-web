package pom.saucedemo;

import framework.web.pom.elements.Button;
import framework.web.pom.elements.Element;
import framework.web.pom.elements.common.ButtonImpl;
import framework.web.pom.elements.common.ElementImpl;
import framework.web.pom.page.BaseView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HeaderView extends BaseView {
  public HeaderView(WebDriver driver) {
    super(driver);
  }

  Element titleElement() {
    return ElementImpl.of(
        this.wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-test='title']"))));
  }

  Button cartButton() {
    return ButtonImpl.of(
        this.wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//*[@data-test='shopping-cart-link']"))));
  }
}
