 package framework.web.reporting;

/*
    Launched via ServiceLauncher declaratively by configuring class name in file
    resources/META_INF/services/org.junit.platform.launcher.TestExecutionListener
 */
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

 public class ExtentWebReportExtension implements TestExecutionListener {

    public static final File REPORT_FILE = new File("build/reports/extent-report/index.html");

    private final ExtentReports extentReport;
    private final File file;
    private final Map<String, ExtentTest> testStatusMap = new ConcurrentHashMap<>();

    public ExtentWebReportExtension() {
        this(REPORT_FILE);
    }

    public ExtentWebReportExtension(File file) {
        this.extentReport = new ExtentReports();
        this.file = file;

        extentReport.attachReporter(new ExtentSparkReporter(file.getAbsolutePath()));
    }

    /*
    add screenshots for tests in report

    restrictions: * only newest screenshot per method is attached
                  * matching is based only on method name without checking package, class and arguments
     */
    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        List<File> screenshots = getScreenshots();
        List<File> videos = getVideoFiles();

        testStatusMap.forEach((k,v) -> {
            screenshots.stream()
                    .filter(f -> f.getName().contains(getMethodName(k)))
                    .max(Comparator.comparingLong(File::lastModified))
                    .ifPresent(f -> v.addScreenCaptureFromPath("./" + f.getName()));

            videos.stream()
                    .filter(f -> f.getName().contains(getMethodName(k)))
                    .max(Comparator.comparingLong(File::lastModified))
                    .ifPresent(f -> {
                        switch(v.getStatus()) {
                            case FAIL:
                                v.fail(MarkupHelper.toTable(new MyVideo(f)));
                                break;
                            case PASS:
                                v.pass(MarkupHelper.toTable(new MyVideo(f)));
                                break;
                        }
                    });
        });

        extentReport.flush();
    }

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        if (!testIdentifier.isTest())
            return;

        String testName = getTestName(testIdentifier.getUniqueId()); //identifier == test name used in screenshots
        ExtentTest extentTest = extentReport.createTest(testName);
        testStatusMap.putIfAbsent(testName, extentTest);
    }

    @Override
    public void executionSkipped(TestIdentifier testIdentifier, String reason) {
        if (!testIdentifier.isTest())
            return;

        String testName = getTestName(testIdentifier.getUniqueId());
        ExtentTest extentTest = extentReport.createTest(testName).skip(reason);
        testStatusMap.putIfAbsent(testName, extentTest);
    }

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {
        if (!testIdentifier.isTest())
            return;

        String testName = getTestName(testIdentifier.getUniqueId());
        TestExecutionResult.Status status = testExecutionResult.getStatus();
        String cause = "Cause unknown";

        if (testExecutionResult.getThrowable().isPresent())
            cause = testExecutionResult.getThrowable().get().toString();

        switch (status) {
            case SUCCESSFUL:
                testStatusMap.get(testName).pass(status.name());
                break;
            case ABORTED:
                testStatusMap.get(testName).log(Status.WARNING, status.name() + ", " + cause);
                break;
            case FAILED:
                testStatusMap.get(testName).fail(status.name() + ", " + cause);
        }
    }

    //segments[0] = engine
    //segments[1] = package + class
    //segments[2] = nested class or method
    //etc...
    private String getTestName(String uniqueId) {
        return extractSegments(uniqueId)
                .stream().skip(1)
                .collect(Collectors.joining("."));
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

     private List<File> getScreenshots() {
         return Arrays.stream(Objects.requireNonNull(file.getParentFile().listFiles()))
                 .filter(f -> f.getName().endsWith(".png"))
                 .collect(Collectors.toList());
     }

     private List<File> getVideoFiles() {
         return Arrays.stream(Objects.requireNonNull(file.getParentFile().listFiles()))
                 .filter(f -> f.getName().endsWith(".mp4") || f.getName().endsWith(".flv"))
                 .collect(Collectors.toList());
     }

     private String getMethodName(String testName) {
         String tmp = testName.substring(0, testName.indexOf("("));

         return tmp.substring(tmp.lastIndexOf(".") + 1);
     }

     private static class MyVideo {

         private final List<String> video;

         MyVideo(File file) {
             String css = "width: 100% !important; height: auto !important;";
             video = Collections.singletonList("<video style=\"" + css + "\" src=\"./" + file.getName() + "\" controls></video>");
         }
     }
}