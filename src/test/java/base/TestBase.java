package base;

import framework.screenplay.actor.Actor;
import framework.web.logging.DriverAugmenter;
import framework.web.logging.SelenoidSupport;
import framework.web.logging.TraceExtension;
import io.github.bonigarcia.seljup.*;
import io.github.glytching.junit.extension.watcher.WatcherExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.WebDriver;
import org.slf4j.bridge.SLF4JBridgeHandler;
import wdm.MyWebDriverManager;

import static framework.web.reporting.ExtentWebReportExtension.REPORT_FILE;

@ExtendWith({WatcherExtension.class})
public class TestBase {

    @RegisterExtension
    static SeleniumJupiter seleniumJupiter = new SeleniumJupiter();

    @RegisterExtension
    static TraceExtension traceExtension = new TraceExtension(); //to set selenoid dev tools forwarded port

    protected WebDriver browser;
    protected DriverAugmenter driverAugmenter;
    protected Actor user;

    static {
        SLF4JBridgeHandler.install(); //required to bridge jul over slf4j (Selenium and WatcherExtensions are using it)
    }

    @BeforeAll
    static void beforeAll() {
        //use custom driver manager to expose selenoid dev tools port in container
        //ignores browser settings from properties
        MyWebDriverManager myWdm = new MyWebDriverManager();
        myWdm.browserInDocker();
        myWdm.browserVersion("107");
        myWdm.enableRecording(); //recording needs to be set here when custom WDM instance in use

        seleniumJupiter.getConfig().setManager(myWdm);
        seleniumJupiter.getConfig().setScreenshot(true);
        seleniumJupiter.getConfig().setRecording(true);
        seleniumJupiter.getConfig().setOutputFolder(REPORT_FILE.getParent());
    }

    //@DockerBrowser(type = CHROME, recording = true) WebDriver driver
    @BeforeEach
    void beforeEach(WebDriver driver) {
        this.browser = driver;
        this.driverAugmenter = new DriverAugmenter(new SelenoidSupport(seleniumJupiter).getDevToolsPort());
        this.user = new Actor();
        traceExtension.setDriverAugmenter(this.driverAugmenter);
    }

    //to use with test containers
    /*
    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        this.browser = createRemoteWebDriver(testInfo);
    }

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
     */
}
