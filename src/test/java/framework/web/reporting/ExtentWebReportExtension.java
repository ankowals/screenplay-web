package framework.web.reporting;

/*
   Launched in a declarative way via ServiceLauncher
   Configure class name in file resources/META_INF/services/org.junit.platform.launcher.TestExecutionListener
*/
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

public class ExtentWebReportExtension implements TestExecutionListener {

  public static final File REPORT_FILE = new File("build/reports/extent-report/index.html");

  private final ExtentReports extentReport;
  private final Map<FqdnTestName, ExtentTest> testStatusMap = new ConcurrentHashMap<>();
  private final MediaAttacher mediaAttacher;

  public ExtentWebReportExtension() {
    this(REPORT_FILE);
  }

  public ExtentWebReportExtension(File file) {
    this.extentReport = new ExtentReports();
    this.extentReport.attachReporter(new ExtentSparkReporter(file.getAbsolutePath()));
    this.mediaAttacher = new MediaAttacher(file);
  }

  /*
  add screenshots for tests in report

  restrictions: * junit 5 nested test class not supported
   */
  @Override
  public void testPlanExecutionFinished(TestPlan testPlan) {
    this.mediaAttacher.attachNestedMedia(this.testStatusMap);
    this.mediaAttacher.attachMedia(this.testStatusMap);
    this.extentReport.flush();
  }

  @Override
  public void executionStarted(TestIdentifier testIdentifier) {
    if (!testIdentifier.isTest()) {
      return;
    }

    FqdnTestName fqdnTestName = new FqdnTestName(testIdentifier);
    ExtentTest extentTest = this.extentReport.createTest(fqdnTestName.asString());

    testIdentifier.getTags().forEach(tag -> extentTest.assignCategory(tag.getName()));

    this.testStatusMap.putIfAbsent(fqdnTestName, extentTest);
  }

  @Override
  public void executionSkipped(TestIdentifier testIdentifier, String reason) {
    if (!testIdentifier.isTest()) {
      return;
    }

    FqdnTestName fqdnTestName = new FqdnTestName(testIdentifier);
    ExtentTest extentTest = this.extentReport.createTest(fqdnTestName.asString()).skip(reason);

    testIdentifier.getTags().forEach(tag -> extentTest.assignCategory(tag.getName()));

    this.testStatusMap.putIfAbsent(fqdnTestName, extentTest);
  }

  @Override
  public void executionFinished(
      TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
    if (!testIdentifier.isTest()) {
      return;
    }

    FqdnTestName fqdnTestName = new FqdnTestName(testIdentifier);
    TestExecutionResult.Status status = testExecutionResult.getStatus();
    String cause = "Cause unknown";

    if (testExecutionResult.getThrowable().isPresent()) {
      cause = testExecutionResult.getThrowable().get().toString();
    }

    switch (status) {
      case SUCCESSFUL:
        this.testStatusMap.get(fqdnTestName).pass(status.name());
        break;
      case ABORTED:
        this.testStatusMap
            .get(fqdnTestName)
            .log(Status.WARNING, String.format("%s, %s", status.name(), cause));
        break;
      case FAILED:
        this.testStatusMap.get(fqdnTestName).fail(String.format("%s, %s", status.name(), cause));
    }
  }
}
