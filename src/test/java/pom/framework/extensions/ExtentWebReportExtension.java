 package pom.framework.extensions;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.junit.jupiter.api.extension.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

 public class ExtentWebReportExtension implements TestWatcher, AfterAllCallback, BeforeTestExecutionCallback {

    private final ExtentReports extentReport;
    private final File file;
    private final Map<String, ExtentTest> testStatusMap = new ConcurrentHashMap<>();

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
    public void afterAll(ExtensionContext context) throws Exception {
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
                    .ifPresent(f -> v.fail(MarkupHelper.toTable(new MyVideo(f))));
        });

        extentReport.flush();
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        String testName = getTestName(context.getUniqueId()); //identifier == test name used in screenshots
        ExtentTest extentTest = extentReport.createTest(getTestName(context.getUniqueId()));
        testStatusMap.putIfAbsent(testName, extentTest);
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        testStatusMap.get(getTestName(context.getUniqueId())).skip(reason.orElse("No reason"));
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        testStatusMap.get(getTestName(context.getUniqueId())).pass(TestResultStatus.SUCCESSFUL.name());
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        testStatusMap.get(getTestName(context.getUniqueId()))
                .log(Status.WARNING, TestResultStatus.ABORTED.name() + ", " + cause.toString());
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        testStatusMap.get(getTestName(context.getUniqueId())).fail(TestResultStatus.FAILED.name() + ", " + cause.toString());
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

    private enum TestResultStatus {
        SUCCESSFUL, ABORTED, FAILED;
    }

     private static class MyVideo {

         private final List<String> video;

         MyVideo(File file) {
             String css = "width: 100% !important; height: auto !important;";
             video = Collections.singletonList("<video style=\"" + css + "\" src=\"./" + file.getName() + "\"></video>");
         }
     }
}