package tests;

import base.TestBase;
import io.github.bonigarcia.seljup.SingleSession;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.html5.WebStorage;

/**
 * In parallel execution each method requires separate driver instance and with @SingleSession enabled we have a single instance shared for all tests.
 * To run test configure parallel execution so that top-level classes run in parallel but methods in same thread.
 *
 * junit.jupiter.execution.parallel.enabled=true
 * junit.jupiter.execution.parallel.mode.default=same_thread
 * junit.jupiter.execution.parallel.mode.classes.default=concurrent
 */
@SingleSession
public class SingleSessionSimpleTest extends TestBase {

    @AfterEach
    void storageCleanup() {
        cleanWebStorage(browser);
    }

    private void cleanWebStorage(WebDriver driver) {
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
