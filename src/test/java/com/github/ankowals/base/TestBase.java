package com.github.ankowals.base;

import com.github.ankowals.framework.reporting.ExtentWebReportExtension;
import com.github.ankowals.framework.web.assertions.logs.LogsAssertionExtension;
import com.github.ankowals.framework.web.assertions.requests.RequestsAssertionExtension;
import com.github.ankowals.framework.web.devtools.DevToolsSupport;
import com.github.ankowals.framework.web.devtools.NetworkDomain;
import com.github.ankowals.framework.web.devtools.PageDomain;
import com.github.ankowals.framework.web.devtools.VideoMerger;
import com.github.ankowals.framework.web.wdm.MyWebDriverManagerFactory;
import com.github.ankowals.framework.web.wdm.bitbucket.BitBucketChromeOptionsFactory;
import io.github.bonigarcia.seljup.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.github.glytching.junit.extension.watcher.WatcherExtension;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.module.LogInspector;
import org.openqa.selenium.bidi.module.Network;
import org.slf4j.bridge.SLF4JBridgeHandler;

@ExtendWith({WatcherExtension.class})
public class TestBase {

  @RegisterExtension
  protected static final SeleniumJupiter SELENIUM_JUPITER = new SeleniumJupiter();

  @RegisterExtension
  protected LogsAssertionExtension logsAssertionExtension = new LogsAssertionExtension();

  @RegisterExtension
  protected RequestsAssertionExtension requestsAssertionExtension =
      new RequestsAssertionExtension();

  @RegisterExtension
  TestExecutionExceptionHandler testExecutionExceptionHandler =
      (context, throwable) -> {
        if (this.browser != null) {
          try {
            Files.write(
                this.toPath(
                    context.getTestClass().orElseThrow(),
                    context.getTestMethod().orElseThrow(),
                    context.getDisplayName(),
                    "png"),
                this.takeScreenshot());
          } catch (NoSuchSessionException ignored) {
          } // NOSONAR browser already closed
          throw throwable;
        }
      };

  protected WebDriver browser;

  static {
    SLF4JBridgeHandler.install(); // required to bridge jul over slf4j
  }

  @BeforeAll
  static void testBaseBeforeAll() {
    SELENIUM_JUPITER
        .getConfig()
        .setManager(MyWebDriverManagerFactory.chrome(BitBucketChromeOptionsFactory.desktop()));

    // does not work well with @SingleSession,
    // screenshots are taken with name of the first test method in the class
    // SELENIUM_JUPITER.getConfig().enableScreenshotWhenFailure();
    // SELENIUM_JUPITER.getConfig().setScreenshotFormat("png");
    SELENIUM_JUPITER.getConfig().setOutputFolder(ExtentWebReportExtension.REPORT_FILE.getParent());

    // works only with docker and does not work in headless
    // recording via BrowserWatcher does not work with devTools if ChromeForTesting not in use
    if (Boolean.parseBoolean(System.getenv("WDM_DOCKERENABLERECORDING"))
        || Boolean.parseBoolean(System.getenv("BROWSER_WATCHER_ENABLED"))) {
      SELENIUM_JUPITER.getConfig().setOutputFolderPerClass(true);
      SELENIUM_JUPITER.getConfig().enableRecording();
      // SELENIUM_JUPITER.getConfig().enableRecordingWhenFailure();
    }
  }

  @BeforeEach
  void testBaseBeforeEach(WebDriver webDriver, TestInfo testInfo) {
    this.browser = webDriver;

    // we can use devTools support like NetworkInterceptor, LogInspector etc or install
    // extension via BiDi but not both at the same time unless chrome for testing in use
    if (!Boolean.parseBoolean(System.getenv("BROWSER_WATCHER_ENABLED"))) {
      this.logsAssertionExtension.logInspector(new LogInspector(this.browser), testInfo);
      this.requestsAssertionExtension.network(new Network(this.browser), testInfo);

      DevToolsSupport devToolsSupport = new DevToolsSupport(this.browser);
      PageDomain pageDomain = devToolsSupport.getPageDomain();
      NetworkDomain networkDomain = devToolsSupport.getNetworkDomain();

      devToolsSupport.createSession();
      pageDomain.enable();
      networkDomain.enable();

      // prefer screencasting for video recording
      if (Boolean.parseBoolean(System.getenv("PAGE_SCREENCASTING_ENABLED"))) {
        pageDomain.startScreencast(testInfo);
      }

      if (Boolean.parseBoolean(System.getenv("TRACING_ENABLED"))) {
        networkDomain.addRequestListener();
        networkDomain.addResponseListener();
      }
    }
  }

  @AfterEach
  void testBaseAfterEach(TestInfo testInfo) throws IOException {
    if (Boolean.parseBoolean(System.getenv("BROWSER_WATCHER_ENABLED"))) {
      this.stopRecording(this.browser, testInfo);
    } else {
      DevToolsSupport devToolsSupport = new DevToolsSupport(this.browser);
      PageDomain pageDomain = devToolsSupport.getPageDomain();

      boolean isScreencastingEnabled =
          Boolean.parseBoolean(System.getenv("PAGE_SCREENCASTING_ENABLED"));

      if (isScreencastingEnabled) {
        pageDomain.stopScreencast();
      }

      // has to be done to allow to attach new screencast listener
      devToolsSupport.clearListeners();

      if (isScreencastingEnabled) {
        File reportDir = ExtentWebReportExtension.REPORT_FILE.getParentFile();

        Path screencastDir = Files.createTempDirectory(reportDir.toPath(), "tmp-");
        boolean result = pageDomain.flush(screencastDir, testInfo);

        // can fail if ffmpeg not installed
        if (result) {
          VideoMerger.merge(screencastDir, this.toPath(testInfo, "mp4"));
        }

        FileUtils.cleanDirectory(screencastDir.toFile());
        FileUtils.deleteDirectory(screencastDir.toFile());
      }
    }
  }

  protected File takeScreenshot(TestInfo testInfo) throws IOException {
    return Files.write(this.toPath(testInfo, "png"), this.takeScreenshot()).toFile();
  }

  protected Path toPath(TestInfo testInfo, String type) {
    return this.toPath(
        testInfo.getTestClass().orElseThrow(),
        testInfo.getTestMethod().orElseThrow(),
        testInfo.getDisplayName(),
        type);
  }

  private byte[] takeScreenshot() {
    return ((TakesScreenshot) this.browser).getScreenshotAs(OutputType.BYTES);
  }

  private File stopRecording(WebDriver webDriver, TestInfo testInfo) throws IOException {
    WebDriverManager wdm = SELENIUM_JUPITER.getConfig().getManager();
    wdm.stopRecording(webDriver);

    return Files.write(
            this.toPath(testInfo, "webm"),
            Base64.getDecoder().decode(wdm.getRecordingPath64(webDriver)))
        .toFile();
  }

  private Path toPath(Class<?> clazz, Method method, String displayName, String type) {
    String fileName =
        "%s.%s[%s].%s"
            .formatted(
                clazz.getName(), method.getName(), this.extractInvocationCount(displayName), type);

    return Path.of(ExtentWebReportExtension.REPORT_FILE.getParent(), fileName);
  }

  private Integer extractInvocationCount(String displayName) {
    Matcher matcher = Pattern.compile("\\[([^]]*)").matcher(displayName);

    if (matcher.find()) {
      return Integer.parseInt(matcher.group(1));
    }

    return 1;
  }
}
