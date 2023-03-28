package framework.web.pom.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BaseView {

    private static final Duration TIMEOUT_10_SECONDS = Duration.ofSeconds(10);
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BaseView(WebDriver driver) {
        this.driver = driver;
        this.wait = createWebDriverWait(TIMEOUT_10_SECONDS);
    }

    protected WebDriverWait createWebDriverWait(Duration timeout) {
        return new WebDriverWait(this.driver, timeout);
    }
    protected WebDriverWait createWebDriverWait(Duration timeout, Duration sleep) {
        return new WebDriverWait(this.driver, timeout, sleep);
    }
}
