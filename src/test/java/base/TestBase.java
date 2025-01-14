package base;

import static framework.web.reporting.ExtentWebReportExtension.REPORT_FILE;

import framework.screenplay.actor.Actor;
import framework.web.tracing.DevToolsTracer;
import framework.web.wdm.MyWebDriverManagerFactory;
import framework.web.wdm.mutators.CapabilitiesMutator;
import io.github.bonigarcia.seljup.*;
import io.github.glytching.junit.extension.watcher.WatcherExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.bridge.SLF4JBridgeHandler;

@ExtendWith({WatcherExtension.class})
public class TestBase {

  @RegisterExtension static SeleniumJupiter seleniumJupiter = new SeleniumJupiter();

  protected WebDriver browser;
  protected DevTools devTools;
  protected Actor user;

  static {
    SLF4JBridgeHandler.install(); // required to bridge jul over slf4j
  }

  @BeforeAll
  static void testBaseBeforeAll() {
    seleniumJupiter.getConfig().setManager(MyWebDriverManagerFactory.chrome());
    seleniumJupiter.getConfig().setOutputFolderPerClass(true);
    seleniumJupiter.getConfig().setScreenshot(true);
    seleniumJupiter.getConfig().setRecording(true);
    seleniumJupiter.getConfig().setOutputFolder(REPORT_FILE.getParent());
  }

  // @DockerBrowser(type = CHROME, recording = true) WebDriver driver
  @BeforeEach
  void testBaseBeforeEach(WebDriver webDriver) throws IllegalAccessException {
    this.browser = webDriver;

    if (this.browser.getClass().isAssignableFrom(RemoteWebDriver.class)) {
      ((RemoteWebDriver) this.browser).setFileDetector(new LocalFileDetector());
      new CapabilitiesMutator().mutate((RemoteWebDriver) this.browser);
      this.browser = new Augmenter().augment(this.browser);
    }

    this.devTools = ((HasDevTools) this.browser).getDevTools();

    new DevToolsTracer(this.devTools).trace();

    this.user = new Actor();
  }

  private void cleanWebStorage(WebDriver driver) {
    try {
      ((JavascriptExecutor) driver).executeScript("window.localStorage.clear()");
    } catch (Exception ignored) {
      // do nothing
    }
  }

  // to use with test containers
  /*
  @BeforeEach
  void beforeEach(TestInfo testInfo) {
      this.browser = createRemoteWebDriver(testInfo);

      if (this.browser.getClass().isAssignableFrom(RemoteWebDriver.class)) {
          ((RemoteWebDriver) this.browser).setFileDetector(new LocalFileDetector());
          new CapabilitiesMutator().mutate((RemoteWebDriver) this.browser);
          this.browser = new Augmenter().augment(this.browser);
      }
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
