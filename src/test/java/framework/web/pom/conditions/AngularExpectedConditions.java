package framework.web.pom.conditions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class AngularExpectedConditions {

    public static List<ExpectedCondition<?>> contentLoaded() {
        return List.of(
                AngularExpectedConditions.stableTestabilities(),
                AngularExpectedConditions.noPendingTasksAndRequests(),
                ExpectedConditions.invisibilityOfElementLocated(By.xpath("//ngx-spinner/child::div")));
    }

    public static ExpectedCondition<Boolean> noPendingTasksAndRequests() {
        return webDriver ->
                Boolean.valueOf(
                        AngularExpectedConditions.executeScript(
                    "return 'getAllAngularTestabilities' in window && "
                           + "0 === window.getAllAngularTestabilities()"
                           + ".map(a => a.getPendingRequestCount() + a.getPendingTasks().length)"
                           + ".reduce((a, b) => a + b, 0)",
                                webDriver));
    }

    public static ExpectedCondition<Boolean> stableTestabilities() {
        return webDriver ->
                Boolean.valueOf(
                    AngularExpectedConditions.executeScript(
                            "return window.getAllAngularTestabilities()"
                                   + ".findIndex(x => !x.isStable()) === -1",
                            webDriver));
    }

    private static String executeScript(String script, WebDriver webDriver) {
        return ((JavascriptExecutor) webDriver).executeScript(script).toString();
    }
}
