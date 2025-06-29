package framework.web.wdm;

import io.github.bonigarcia.wdm.webdriver.WebDriverBrowser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.JavascriptExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

record MyWebDriverBrowser(WebDriverBrowser webDriverBrowser, String recordingDir) {

  private static final Logger LOGGER = LoggerFactory.getLogger(MyWebDriverBrowser.class);

  public void stopRecording() {
    ((JavascriptExecutor) this.webDriverBrowser.getDriver())
        .executeScript("window.postMessage({ type: \"stopRecording\" }, \"*\");");
    this.waitForRecording(Paths.get(this.recordingDir, this.webDriverBrowser.getRecordingName()));
  }

  private void waitForRecording(Path filePath) {
    // in case before hooks will fail and startRecording won't be called
    if (filePath.endsWith("null.webm")) {
      LOGGER.warn("Recording name unknown!");
      return;
    }

    try {
      Awaitility.await()
          .atMost(Duration.ofSeconds(5))
          .pollInterval(Duration.ofMillis(500))
          .ignoreExceptions()
          .until(() -> Files.exists(filePath));

      this.webDriverBrowser.setRecordingPath(filePath);
    } catch (ConditionTimeoutException e) {
      LOGGER.warn("Timeout of {} seconds reached, recording {} not found", 5, filePath);
    }
  }
}
