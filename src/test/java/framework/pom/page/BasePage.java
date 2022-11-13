package framework.pom.page;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

public class BasePage {

    /* alternatively set driver thorough setter and init via default constructor */
    protected final WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getTitle() {
        return driver.getTitle();
    }
    public void open(String url) {
        driver.get(url);
    }
    public byte[] takeScreenshot() { return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES); }

    protected Actions createAction() { return new Actions(driver); }
    protected WebDriver.Navigation navigate() { return driver.navigate(); }
    protected WebDriver.TargetLocator switchTo() { return driver.switchTo(); }

    protected boolean isInsideFrame() {
        return ((JavascriptExecutor) driver).executeScript("return window.frameElement") != null;
    }
}
