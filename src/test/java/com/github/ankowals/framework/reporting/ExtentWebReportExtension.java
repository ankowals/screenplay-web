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
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.jspecify.annotations.NonNull;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

public class ExtentWebReportExtension implements TestExecutionListener {

  public static final File REPORT_FILE = ExtentWebReportExtension.reportFile();

  private final ExtentReports extentReport;
  private final Map<FqdnTestName, ExtentTest> testStatusMap = new ConcurrentHashMap<>();
  private final MediaAttacher mediaAttacher;

  public ExtentWebReportExtension() {
    this.extentReport = new ExtentReports();
    this.extentReport.attachReporter(new ExtentSparkReporter(REPORT_FILE.getAbsolutePath()));
    this.mediaAttacher = new MediaAttacher(REPORT_FILE);
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
    // include failed configuration methods in the report
    if (!testIdentifier.isTest()) {
      if (testExecutionResult.getThrowable().isPresent()) {
        FqdnTestName fqdnTestName = new FqdnTestName(testIdentifier);
        ExtentTest extentTest = this.extentReport.createTest(fqdnTestName.asString());
        testIdentifier.getTags().forEach(tag -> extentTest.assignCategory(tag.getName()));
        this.testStatusMap.putIfAbsent(fqdnTestName, extentTest);
      } else {
        return;
      }
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

  private static File reportFile() {
    URL initLocation =
        ExtentWebReportExtension.class.getProtectionDomain().getCodeSource().getLocation();

    File targetOrBuildDir =
        ExtentWebReportExtension.doGetTargetOrBuildDir(new File(initLocation.getPath()));

    File reportFile =
        new File(
            String.format("%s/extent-report", targetOrBuildDir.getAbsolutePath()), "index.html");

    if (!reportFile.getParentFile().exists()) {
      reportFile.getParentFile().mkdirs();
    }

    return reportFile;
  }

  private static File doGetTargetOrBuildDir(File file) {
    if (file == null || !file.exists()) {
      throw new IllegalStateException("target or build directory not found!");
    }

    if (file.isDirectory() && (file.getName().equals("target") || file.getName().equals("build"))) {
      return file;
    }

    return ExtentWebReportExtension.doGetTargetOrBuildDir(file.getParentFile());
  }
}
