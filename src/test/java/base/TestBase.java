package base;

import framework.screenplay.actor.Actor;
import framework.web.reporting.ExtentWebReportExtension;
import framework.web.tracing.DevToolsTracer;
import framework.web.wdm.ChromeOptionsFactory;
import framework.web.wdm.MyWebDriverManagerFactory;
import framework.web.wdm.RecordingEnabler;
import io.github.bonigarcia.seljup.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.glytching.junit.extension.watcher.WatcherExtension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.HasDevTools;
import org.slf4j.bridge.SLF4JBridgeHandler;

@ExtendWith({WatcherExtension.class})
public class TestBase {

  @RegisterExtension static final SeleniumJupiter SELENIUM_JUPITER = new SeleniumJupiter();

  protected WebDriver browser;
  protected Actor user;

  static {
    SLF4JBridgeHandler.install(); // required to bridge jul over slf4j
  }

  @BeforeAll
  static void testBaseBeforeAll() {
    SELENIUM_JUPITER
        .getConfig()
        .setManager(MyWebDriverManagerFactory.chrome(ChromeOptionsFactory.desktop()));
    SELENIUM_JUPITER.getConfig().enableScreenshotWhenFailure();
    SELENIUM_JUPITER.getConfig().setScreenshotFormat("png");
    // SELENIUM_JUPITER.getConfig().enableRecording();
    SELENIUM_JUPITER.getConfig().enableRecordingWhenFailure();

    // works only with docker and does not work in headless
    if (Boolean.parseBoolean(System.getenv("WDM_DOCKERENABLERECORDING"))) {
      SELENIUM_JUPITER.getConfig().setRecording(true);
    }

    SELENIUM_JUPITER.getConfig().setOutputFolderPerClass(true);
    SELENIUM_JUPITER.getConfig().setOutputFolder(ExtentWebReportExtension.REPORT_FILE.getParent());
  }

  // @DockerBrowser(type = CHROME, recording = true) WebDriver driver
  @BeforeEach
  void testBaseBeforeEach(WebDriver webDriver) {
    this.browser = webDriver;

    // we can use devTools support like NetworkInterceptor, LogInspector etc or install
    // extension via BiDi but not both at the same time
    if (!Boolean.parseBoolean(System.getenv("BROWSER_WATCHER_ENABLED"))) {
      new DevToolsTracer(((HasDevTools) this.browser).getDevTools()).trace();
    }

    this.user = new Actor();
  }

  @AfterEach
  void testBaseAfterEach(TestInfo testInfo) throws IOException {
    if (Boolean.parseBoolean(System.getenv("BROWSER_WATCHER_ENABLED"))) {
      File recording = this.stopRecording(this.browser, testInfo);
    }
  }

  protected byte[] takeScreenshot() {
    return ((TakesScreenshot) this.browser).getScreenshotAs(OutputType.BYTES);
  }

  protected File writeImage(byte[] bytes, TestInfo testInfo) throws IOException {
    return this.doWrite(bytes, this.formatFilePath(testInfo, "png"));
  }

  protected RecordingEnabler startRecording(WebDriver webDriver) {
    return () -> SELENIUM_JUPITER.getConfig().getManager().startRecording(webDriver);
  }

  private File stopRecording(WebDriver webDriver, TestInfo testInfo) throws IOException {
    WebDriverManager wdm = SELENIUM_JUPITER.getConfig().getManager();
    wdm.stopRecording(webDriver);

    return this.doWrite(
        Base64.getDecoder().decode(wdm.getRecordingPath64(webDriver)),
        this.formatFilePath(testInfo, "webm"));
  }

  private File doWrite(byte[] bytes, String name) throws IOException {
    if (!Files.exists(ExtentWebReportExtension.REPORT_FILE.getParentFile().toPath())) {
      ExtentWebReportExtension.REPORT_FILE.getParentFile().mkdir();
    }

    File file = Path.of(ExtentWebReportExtension.REPORT_FILE.getParent(), name).toFile();
    Files.write(file.toPath(), bytes);

    return file;
  }

  private String formatFilePath(TestInfo testInfo, String type) {
    return String.format(
        "%s/%s-%s.%s",
        testInfo.getTestClass().orElseThrow().getName(),
        testInfo.getTestMethod().orElseThrow().getName(),
        UUID.randomUUID(),
        type);
  }
}
