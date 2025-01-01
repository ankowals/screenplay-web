package pom.automationpractice.views;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import framework.web.pom.elements.*;
import framework.web.pom.elements.common.ButtonImpl;
import framework.web.pom.elements.common.DropDownImpl;
import framework.web.pom.elements.common.InputImpl;
import framework.web.pom.page.BaseView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AuthenticationView extends BaseView {

  public AuthenticationView(WebDriver driver) {
    super(driver);
  }

  public Input getEmailInput() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//input[@id='email_create']")));
    return InputImpl.of(element);
  }

  public Button getCreateAccountButton() {
    WebElement element =
        this.wait.until(elementToBeClickable(By.xpath("//button[@id='SubmitCreate']")));
    return ButtonImpl.of(element);
  }

  public Input getFirstNameInput() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//input[@id='customer_firstname']")));
    return InputImpl.of(element);
  }

  public Input getLastNameInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='customer_lastname']")));
  }

  public Input getPasswordInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='passwd']")));
  }

  public Input getAddressInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='address1']")));
  }

  public Input getCityInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='city']")));
  }

  public Input getPostcodeInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='postcode']")));
  }

  public Input getMobilePhoneInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='phone_mobile']")));
  }

  public Input getAliasInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='alias']")));
  }

  public Dropdown getStateSelect() {
    return DropDownImpl.of(this.driver.findElement(By.xpath("//select[@id='id_state']")));
  }

  public Dropdown getCountrySelect() {
    return DropDownImpl.of(this.driver.findElement(By.xpath("//select[@id='id_country']")));
  }

  public Button getRegisterButton() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//button[@id='submitAccount']")));
    return ButtonImpl.of(element);
  }
}
