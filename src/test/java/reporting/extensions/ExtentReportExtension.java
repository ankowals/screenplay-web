package reporting.extensions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtentReportExtension implements TestWatcher, AfterAllCallback, BeforeAllCallback {

    public static final String REPORT_PATH = "build/reports/extent-report";

    private ExtentReports extentReport;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        ExtentSparkReporter reporter = new ExtentSparkReporter(REPORT_PATH + "/index.html");
        extentReport = new ExtentReports();
        extentReport.attachReporter(reporter);
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        extentReport.flush();
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        extentReport.createTest(context.getDisplayName()).skip(reason.orElse("No reason"));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        extentReport.createTest(getTestName(context.getUniqueId())).pass(TestResultStatus.SUCCESSFUL.name());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        extentReport.createTest(getTestName(context.getUniqueId()))
                .log(Status.WARNING, TestResultStatus.ABORTED.name() + ", " + cause.toString());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        extentReport.createTest(getTestName(context.getUniqueId()))
                .fail(TestResultStatus.FAILED.name() + ", " + cause.toString());
    }

    private String getTestName(String uniqueId) {
        List<String> segments = extractSegments(uniqueId);
        return segments.get(1) + "." + segments.get(2);
    }

    private List<String> extractSegments(String source) {
        Pattern p = Pattern.compile("\\[(.*?)\\]");
        Matcher m = p.matcher(source);

        List<String> segments = new ArrayList<>();

        while(m.find()) {
            String src = m.group(1);
            segments.add(src.substring(src.indexOf(':') + 1));
        }

        return segments;
    }

    private enum TestResultStatus {
        SUCCESSFUL, ABORTED, FAILED;
    }
}