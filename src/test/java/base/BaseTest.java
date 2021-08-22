package base;

import io.github.bonigarcia.seljup.*;
import io.github.glytching.junit.extension.watcher.WatcherExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.html5.WebStorage;
import org.slf4j.bridge.SLF4JBridgeHandler;
import java.util.Collections;

@ExtendWith({SeleniumJupiter.class, WatcherExtension.class})
public class BaseTest {

    static {
        SLF4JBridgeHandler.install(); //required to bridge jul over slf4j (Selenium and WatcherExtensions are using it)
    }

    protected WebDriver browser;

    @BeforeEach
    void beforeEach(WebDriver driver) {
        this.browser = driver;
    }

    @Options
    ChromeOptions chromeOptions = new ChromeOptions();

    @Options
    EdgeOptions edgeOptions = new EdgeOptions();

    {
        chromeOptions.addArguments("start-maximized", "no-default-browser-check");
        chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
    }

    protected void cleanWebStorage(WebDriver driver) {
        if (driver instanceof WebStorage) {
            WebStorage webStorage = (WebStorage) driver;
            webStorage.getSessionStorage().clear();
            webStorage.getLocalStorage().clear();
        } else {
            try {
                ((JavascriptExecutor) driver).executeScript("window.localStorage.clear()");
            } catch (Exception ignored) {
                //do nothing
            }
        }
    }
}
