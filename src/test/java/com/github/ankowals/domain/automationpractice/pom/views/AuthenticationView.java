package com.github.ankowals.domain.automationpractice.pom.views;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import com.github.ankowals.framework.web.pom.elements.*;
import com.github.ankowals.framework.web.pom.elements.common.ButtonImpl;
import com.github.ankowals.framework.web.pom.elements.common.DropDownImpl;
import com.github.ankowals.framework.web.pom.elements.common.InputImpl;
import com.github.ankowals.framework.web.pom.page.BaseView;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AuthenticationView extends BaseView {

  public AuthenticationView(WebDriver driver) {
    super(driver);
  }

  public Input emailInput() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//input[@id='email_create']")));
    return InputImpl.of(element);
  }

  public Button createAccountButton() {
    WebElement element =
        this.wait.until(elementToBeClickable(By.xpath("//button[@id='SubmitCreate']")));
    return ButtonImpl.of(element);
  }

  public Input firstNameInput() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//input[@id='customer_firstname']")));
    return InputImpl.of(element);
  }

  public Input lastNameInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='customer_lastname']")));
  }

  public Input passwordInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='passwd']")));
  }

  public Input addressInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='address1']")));
  }

  public Input cityInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='city']")));
  }

  public Input postcodeInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='postcode']")));
  }

  public Input mobilePhoneInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='phone_mobile']")));
  }

  public Input aliasInput() {
    return InputImpl.of(this.driver.findElement(By.xpath("//input[@id='alias']")));
  }

  public Dropdown stateSelect() {
    return DropDownImpl.of(this.driver.findElement(By.xpath("//select[@id='id_state']")));
  }

  public Dropdown countrySelect() {
    return DropDownImpl.of(this.driver.findElement(By.xpath("//select[@id='id_country']")));
  }

  public Button registerButton() {
    WebElement element =
        this.wait.until(visibilityOfElementLocated(By.xpath("//button[@id='submitAccount']")));
    return ButtonImpl.of(element);
  }
}
