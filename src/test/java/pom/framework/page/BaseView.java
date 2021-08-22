package pom.framework.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BaseView {

    private static final long TIMEOUT_10_SECONDS = 10;
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BaseView(WebDriver driver) {
        this.driver = driver;
        this.wait = createWebDriverWait(TIMEOUT_10_SECONDS);
    }

    protected WebDriverWait createWebDriverWait(long timeout) {
        return new WebDriverWait(driver, timeout);
    }
    protected WebDriverWait createWebDriverWait(long timeout, long sleep) {
        return new WebDriverWait(driver, timeout, sleep);
    }
}
