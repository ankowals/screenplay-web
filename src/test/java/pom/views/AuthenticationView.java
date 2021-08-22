package pom.views;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pom.framework.elements.*;
import pom.framework.page.BaseView;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class AuthenticationView extends BaseView {

    private static final By EMAIL_INPUT = By.xpath("//input[@id='email_create']");
    private static final By FIRST_NAME_INPUT = By.xpath("//input[@id='customer_firstname']");
    private static final By LAST_NAME_INPUT = By.xpath("//input[@id='customer_lastname']");
    private static final By PASSWORD_INPUT = By.xpath("//input[@id='passwd']");
    private static final By ADDRESS_INPUT = By.xpath("//input[@id='address1']");
    private static final By CITY_INPUT = By.xpath("//input[@id='city']");
    private static final By POSTCODE_INPUT = By.xpath("//input[@id='postcode']");
    private static final By MOBILE_PHONE_INPUT = By.xpath("//input[@id='phone_mobile']");
    private static final By ALIAS_INPUT = By.xpath("//input[@id='alias']");

    private static final By STATE_SELECT = By.xpath("//select[@id='id_state']");
    private static final By COUNTRY_SELECT = By.xpath("//select[@id='id_country']");

    private static final By CREATE_ACCOUNT_BUTTON = By.xpath("//button[@id='SubmitCreate']");
    private static final By REGISTER_BUTTON = By.xpath("//button[@id='submitAccount']");

    public AuthenticationView(WebDriver driver) {
        super(driver);
    }

    public Input getEmailInput() {
        WebElement element = wait.until(visibilityOfElementLocated(EMAIL_INPUT));
        return InputImpl.of(element);
    }
    public Button getCreateAccountButton() {
        WebElement element = wait.until(elementToBeClickable(CREATE_ACCOUNT_BUTTON));
        return ButtonImpl.of(element);
    }

    public Input getFirstNameInput() {
        WebElement element = wait.until(visibilityOfElementLocated(FIRST_NAME_INPUT));
        return InputImpl.of(element);
    }

    public Input getLastNameInput() { return InputImpl.of(driver.findElement(LAST_NAME_INPUT)); }
    public Input getPasswordInput() { return InputImpl.of(driver.findElement(PASSWORD_INPUT)); }
    public Input getAddressInput() { return InputImpl.of(driver.findElement(ADDRESS_INPUT)); }
    public Input getCityInput() { return InputImpl.of(driver.findElement(CITY_INPUT)); }
    public Input getPostcodeInput() { return InputImpl.of(driver.findElement(POSTCODE_INPUT)); }
    public Input getMobilePhoneInput() { return InputImpl.of(driver.findElement(MOBILE_PHONE_INPUT)); }
    public Input getAliasInput() { return InputImpl.of(driver.findElement(ALIAS_INPUT)); }

    public Dropdown getStateSelect() { return DropDownImpl.of(driver.findElement(STATE_SELECT)); }
    public Dropdown getCountrySelect() { return DropDownImpl.of(driver.findElement(COUNTRY_SELECT)); }

    public Button getRegisterButton() {
        WebElement element = wait.until(visibilityOfElementLocated(REGISTER_BUTTON));
        return ButtonImpl.of(element);
    }
}
