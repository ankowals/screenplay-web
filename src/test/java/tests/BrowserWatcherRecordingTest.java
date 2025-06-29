package tests;

import base.TestBase;
import framework.screenplay.actor.Actor;
import framework.screenplay.helpers.See;
import framework.web.reporting.ExtentWebReportExtension;
import framework.web.screenplay.BrowseTheWeb;
import java.io.IOException;
import java.nio.file.*;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import screenplay.saucedemo.interactions.Login;
import screenplay.saucedemo.questions.TheErrorMessage;

class BrowserWatcherRecordingTest extends TestBase {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestBase.class);

  Actor user;

  @BeforeEach
  void beforeEach() {
    this.user = new Actor();
    this.user.can(BrowseTheWeb.with(this.browser));
  }

  @AfterEach
  void afterEach() throws IOException {
    this.stopRecording(this.browser);
    Path recordingPath = this.browserWatcherRecordingPath(this.browser);

    if (recordingPath != null) {
      try {
        Files.copy(
            recordingPath,
            Paths.get(
                ExtentWebReportExtension.REPORT_FILE.getParentFile().getAbsolutePath(),
                recordingPath.getFileName().toString()),
            StandardCopyOption.REPLACE_EXISTING);
      } catch (NoSuchFileException e) {
        LOGGER.warn("Could not find file {}", recordingPath);
      }
    }

    // video copied to the report, original not needed any more
    if (recordingPath != null) {
      Files.deleteIfExists(recordingPath);
    }
  }

  // recording has to be started after opening url in the browser
  // active tab is recorded
  @Test
  void shouldSeeMessageWhenPasswordIsWrong(TestInfo testInfo) throws Exception {
    this.user.attemptsTo(
        Login.with(
            new Login.Credentials("standard_user", "terefere"),
            this.startRecording(this.browser, testInfo)));

    this.user.should(
        See.that(
            TheErrorMessage.uponLogin(),
            Matchers.containsString(
                "Username and password do not match any user in this service")));
  }
}
