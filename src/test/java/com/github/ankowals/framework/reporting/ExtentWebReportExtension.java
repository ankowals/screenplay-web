package com.github.ankowals.framework.reporting;

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
import org.jspecify.annotations.NonNull;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

public class ExtentWebReportExtension implements TestExecutionListener {

  public static final File REPORT_FILE = ExtentWebReportExtension.getReportFile();

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
  add screenshots for com.github.ankowals.tests in report

  restrictions: * junit 5 nested test class not supported
   */
  @Override
  public void testPlanExecutionFinished(@NonNull TestPlan testPlan) {
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
  public void executionSkipped(TestIdentifier testIdentifier, @NonNull String reason) {
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
      TestIdentifier testIdentifier, @NonNull TestExecutionResult testExecutionResult) {
    if (!testIdentifier.isTest()) {
      return;
    }

    FqdnTestName fqdnTestName = new FqdnTestName(testIdentifier);
    TestExecutionResult.Status status = testExecutionResult.getStatus();
    Optional<Throwable> maybeThrowable = testExecutionResult.getThrowable();

    switch (status) {
      case SUCCESSFUL:
        this.testStatusMap.get(fqdnTestName).pass(status.name());
        break;
      case ABORTED:
        this.testStatusMap
            .get(fqdnTestName)
            .log(Status.WARNING, maybeThrowable.orElseGet(() -> new AssertionError("Aborted")));
        break;
      case FAILED:
        this.testStatusMap
            .get(fqdnTestName)
            .fail(maybeThrowable.orElseGet(() -> new AssertionError("Cause unknown")));
    }
  }

  private static File getReportFile() {
    String targetOrBuildDir =
        new File(
                ExtentWebReportExtension.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
            .getParent();

    return new File(String.format("%s/reports/extent-report", targetOrBuildDir), "index.html");
  }
}
