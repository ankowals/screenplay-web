package framework.web.pom.page;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

public class BasePage {

    /* alternatively set driver thorough setter and init via default constructor */
    protected final WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    public String getTitle() {
        return this.driver.getTitle();
    }
    public void open(String url) {
        this.driver.get(url);
    }
    public byte[] takeScreenshot() { return ((TakesScreenshot) this.driver).getScreenshotAs(OutputType.BYTES); }

    protected Actions createAction() { return new Actions(this.driver); }
    protected WebDriver.Navigation navigate() { return this.driver.navigate(); }
    protected WebDriver.TargetLocator switchTo() { return this.driver.switchTo(); }

    protected boolean isInsideFrame() {
        return ((JavascriptExecutor) this.driver).executeScript("return window.frameElement") != null;
    }
}
