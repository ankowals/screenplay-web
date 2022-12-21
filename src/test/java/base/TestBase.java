package base;

import io.github.bonigarcia.seljup.*;
import io.github.glytching.junit.extension.watcher.WatcherExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;
import org.slf4j.bridge.SLF4JBridgeHandler;

import static reporting.ExtentWebReportExtension.REPORT_FILE;

@ExtendWith({WatcherExtension.class})
public class TestBase {

    @RegisterExtension
    static SeleniumJupiter seleniumJupiter = new SeleniumJupiter();

    protected WebDriver browser;

    static {
        SLF4JBridgeHandler.install(); //required to bridge jul over slf4j (Selenium and WatcherExtensions are using it)
    }

    @BeforeAll
    static void beforeAll() {
        seleniumJupiter.getConfig().setScreenshotWhenFailure(true);
        seleniumJupiter.getConfig().setRecordingWhenFailure(true);
        seleniumJupiter.getConfig().setOutputFolder(REPORT_FILE.getAbsolutePath());
    }

    @BeforeEach
    void beforeEach(WebDriver driver) {
        this.browser = driver;
    }
}
