package framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.extension.*;

public class ExtentReportExtension implements TestWatcher, AfterAllCallback {

  private final ExtentReports extentReport;

  public ExtentReportExtension(File file) {
    this.extentReport = new ExtentReports();
    this.extentReport.attachReporter(new ExtentSparkReporter(file.getAbsolutePath()));
  }

  @Override
  public void afterAll(ExtensionContext context) throws Exception {
    this.extentReport.flush();
  }

  @Override
  public void testDisabled(ExtensionContext context, Optional<String> reason) {
    this.extentReport.createTest(context.getDisplayName()).skip(reason.orElse("No reason"));
  }

  @Override
  public void testSuccessful(ExtensionContext context) {
    this.extentReport
        .createTest(this.getTestName(context.getUniqueId()))
        .pass(TestResultStatus.SUCCESSFUL.name());
  }

  @Override
  public void testAborted(ExtensionContext context, Throwable cause) {
    this.extentReport
        .createTest(this.getTestName(context.getUniqueId()))
        .log(Status.WARNING, TestResultStatus.ABORTED.name() + ", " + cause.toString());
  }

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    this.extentReport
        .createTest(this.getTestName(context.getUniqueId()))
        .fail(TestResultStatus.FAILED.name() + ", " + cause.toString());
  }

  private String getTestName(String uniqueId) {
    List<String> segments = this.extractSegments(uniqueId);
    return segments.get(1) + "." + segments.get(2);
  }

  private List<String> extractSegments(String source) {
    Pattern p = Pattern.compile("\\[(.*?)\\]");
    Matcher m = p.matcher(source);

    List<String> segments = new ArrayList<>();

    while (m.find()) {
      String src = m.group(1);
      segments.add(src.substring(src.indexOf(':') + 1));
    }

    return segments;
  }

  private enum TestResultStatus {
    SUCCESSFUL,
    ABORTED,
    FAILED;
  }
}
