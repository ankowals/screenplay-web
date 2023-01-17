package base;

import framework.web.logging.TraceExtension;
import io.github.bonigarcia.seljup.*;
import io.github.glytching.junit.extension.watcher.WatcherExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.VncRecordingContainer;
import org.testcontainers.lifecycle.TestDescription;

import java.util.Optional;

import static framework.web.reporting.ExtentWebReportExtension.REPORT_FILE;
import static io.github.bonigarcia.seljup.BrowserType.CHROME;

@ExtendWith({WatcherExtension.class})
public class TestBase {

    @RegisterExtension
    static SeleniumJupiter seleniumJupiter = new SeleniumJupiter();

    @RegisterExtension
    static TraceExtension traceExtension = new TraceExtension(); //to set selenoid dev tools forwarded port

    protected WebDriver browser;

    static {
        SLF4JBridgeHandler.install(); //required to bridge jul over slf4j (Selenium and WatcherExtensions are using it)
    }

    @BeforeAll
    static void beforeAll() {
        seleniumJupiter.getConfig().setScreenshotWhenFailure(true);
        seleniumJupiter.getConfig().setRecordingWhenFailure(true);
        seleniumJupiter.getConfig().setOutputFolder(REPORT_FILE.getParentFile().getAbsolutePath());

        //use custom driver manager to expose selenoid dev tools port in container
        MyWebDriverManager myWdm = new MyWebDriverManager();
        seleniumJupiter.getConfig().setManager(myWdm);
    }

    //@DockerBrowser(type = CHROME, recording = true) WebDriver driver
    @BeforeEach
    void beforeEach(WebDriver driver) {
        this.browser = driver;

        //turn on tracing for selenoid based images
        String containerId = seleniumJupiter.getConfig().getManager().getDockerBrowserContainerId();
        String devToolsPort = seleniumJupiter.getConfig().getManager().getDockerService().getBindPort(containerId, "7070");
        traceExtension.setSelenoidDevToolsPort(devToolsPort);
    }

    /*
    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        this.browser = createRemoteWebDriver(testInfo);
    }
     */

    //to use with test containers
    private RemoteWebDriver createRemoteWebDriver(TestInfo testInfo) {
        BrowserWebDriverContainer<?> container = new BrowserWebDriverContainer<>()
                .withCapabilities(new ChromeOptions())
                .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL,
                        REPORT_FILE.getParentFile().getAbsoluteFile(),
                        VncRecordingContainer.VncRecordingFormat.MP4);

        container.start();

        container.afterTest(
                new TestDescription() {
                    @Override
                    public String getTestId() {
                        return getFilesystemFriendlyName();
                    }

                    @Override
                    public String getFilesystemFriendlyName() {
                        return testInfo.getTestMethod().orElse(null).getName();
                    }
                },
                Optional.empty()
        );

        return container.getWebDriver();
    }

}
