package framework.web.accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.AxeBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.UUID;
import java.util.function.Consumer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;

public class AccessibilityAssertions implements MandatoryReportAs {

  private final WebDriver webDriver;
  private final AxeBuilder axeBuilder;

  private TestInfo testInfo;
  private File reportParentDir;

  private AccessibilityAssertions(WebDriver webDriver, AxeBuilder axeBuilder) {
    this.webDriver = webDriver;
    this.axeBuilder = axeBuilder;
  }

  public static MandatoryReportAs assertThat(WebDriver webDriver) {
    return new AccessibilityAssertions(webDriver, new AxeBuilder());
  }

  @Override
  public AccessibilityAssertions reportAs(File reportParentDir, TestInfo testInfo) {
    this.reportParentDir = reportParentDir;
    this.testInfo = testInfo;
    return this;
  }

  public void isViolationFree() throws IOException, ParseException {
    if (System.getenv("ACCESSIBILITY_ASSERTIONS_ENABLED") == null) {
      return;
    }

    Results results = this.axeBuilder.analyze(this.webDriver);

    // File file = this.createTmpFile();
    // AxeReporter.writeResultsToJsonFile(file.getAbsolutePath(), axeResults);

    new HtmlReportCreator(results)
        .create(
            String.format("%s/%s", this.reportParentDir, this.reportFileName(this.testInfo)),
            this.reportName(this.testInfo));

    Assertions.assertThat(results.violationFree()).isTrue();
  }

  public AccessibilityAssertions customizeAxe(Consumer<AxeBuilder> customizer) {
    customizer.accept(this.axeBuilder);
    return this;
  }

  private String reportFileName(TestInfo testInfo) {
    return String.format(
        "%s/%s-axe-%s.html",
        testInfo.getTestClass().orElseThrow().getName(),
        testInfo.getTestMethod().orElseThrow().getName(),
        UUID.randomUUID());
  }

  private String reportName(TestInfo testInfo) {
    return String.format(
        "%s.%s",
        testInfo.getTestClass().orElseThrow().getName(),
        testInfo.getTestMethod().orElseThrow().getName());
  }

  private File createTmpFile() throws IOException {
    File file = Files.createTempFile("tmp-axe-report-", ".json").toFile();
    file.deleteOnExit();

    return file;
  }
}
