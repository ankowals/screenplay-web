package base;

import io.github.bonigarcia.seljup.*;
import io.github.glytching.junit.extension.watcher.WatcherExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;
import org.slf4j.bridge.SLF4JBridgeHandler;
import pom.framework.extensions.ExtentWebReportExtension;

import java.io.File;

@ExtendWith({WatcherExtension.class})
public class BaseTest {

    private final static File reportFile = new File("build/reports/extent-report/index.html");

    @RegisterExtension
    static SeleniumJupiter seleniumJupiter = new SeleniumJupiter();

    @RegisterExtension
    static ExtentWebReportExtension extentCachedWebReportExtension = new ExtentWebReportExtension(reportFile);

    protected WebDriver browser;

    static {
        SLF4JBridgeHandler.install(); //required to bridge jul over slf4j (Selenium and WatcherExtensions are using it)
    }

    @BeforeAll
    static void beforeAll() {
        seleniumJupiter.getConfig().setScreenshot(false);
        seleniumJupiter.getConfig().setScreenshotWhenFailure(true);
        seleniumJupiter.getConfig().setRecording(false);
        seleniumJupiter.getConfig().setRecordingWhenFailure(true);
        seleniumJupiter.getConfig().setOutputFolder(reportFile.getAbsolutePath());
    }

    @BeforeEach
    void beforeEach(WebDriver driver) {
        this.browser = driver;
    }
}
