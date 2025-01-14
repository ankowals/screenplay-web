package framework.reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.ExceptionInfo;
import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.Test;
import com.aventstack.extentreports.model.service.ExceptionInfoService;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.extension.*;

public class ExtentReportExtension implements TestWatcher, BeforeTestExecutionCallback {

  private static final AtomicBoolean HAS_RUN = new AtomicBoolean(false);

  protected final ExtentReports extentReport;

  public ExtentReportExtension(File file) {
    this.extentReport = new ExtentReports();
    this.extentReport.attachReporter(new ExtentSparkReporter(file.getAbsolutePath()));

    if (HAS_RUN.compareAndSet(false, true)) {
      Runtime.getRuntime().addShutdownHook(new Thread(this::doFlush));
    }
  }

  @Override
  public void beforeTestExecution(ExtensionContext context) throws Exception {
    this.extentReport.createTest(this.getTestName(context.getUniqueId()));
  }

  @Override
  public void testDisabled(ExtensionContext context, Optional<String> reason) {
    this.extentReport
        .createTest(this.getTestName(context.getUniqueId()))
        .skip(reason.orElse("no reason"));
  }

  @Override
  public void testSuccessful(ExtensionContext context) {
    Test test =
        this.extentReport
            .getReport()
            .findTest(this.getTestName(context.getUniqueId()))
            .orElseThrow();

    test.setStatus(Status.PASS);
  }

  @Override
  public void testAborted(ExtensionContext context, Throwable cause) {
    Test test =
        this.extentReport
            .getReport()
            .findTest(this.getTestName(context.getUniqueId()))
            .orElseGet(
                () ->
                    this.extentReport
                        .createTest(this.getTestName(context.getUniqueId()))
                        .getModel());

    test.setStatus(Status.FAIL);
    ExceptionInfo exceptionInfo = ExceptionInfo.builder().exception(cause).build();
    Log log = Log.builder().details("Aborted").exception(exceptionInfo).build();
    test.addLog(log);
  }

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    Test test =
        this.extentReport
            .getReport()
            .findTest(this.getTestName(context.getUniqueId()))
            .orElseThrow();
    ExceptionInfo exceptionInfo = ExceptionInfoService.createExceptionInfo(cause);

    test.setStatus(Status.FAIL);
    test.getExceptions().add(exceptionInfo);

    Log log = Log.builder().details("").status(Status.FAIL).exception(exceptionInfo).build();

    test.addLog(log);
  }

  private String getTestName(String uniqueId) {
    List<String> segments = this.extractSegments(uniqueId);
    return segments.get(1) + "." + segments.get(2).substring(0, segments.get(2).indexOf("("));
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

  private void doFlush() {
    this.extentReport.flush();
  }
}
