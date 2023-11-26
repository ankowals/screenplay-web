package framework.web.pom.conditions;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class AngularExpectedConditions {

    public static List<ExpectedCondition<?>> contentLoaded() {
        return List.of(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//ngx-spinner/child::div")),
                AngularExpectedConditions.noPendingTasksAndRequests(),
                ExpectedConditions.invisibilityOfElementLocated(By.xpath("//ngx-spinner/child::div")));
    }

    public static ExpectedCondition<Boolean> noPendingTasksAndRequests() {
        return webDriver -> {
            assert webDriver != null;
            return Boolean.valueOf(((JavascriptExecutor) webDriver)
                    .executeScript("return 'getAllAngularTestabilities' in window && " +
                            "0 === window.getAllAngularTestabilities().map(a => a.getPendingRequestCount() + a.getPendingTasks().length).reduce((a, b) => a + b, 0)")
                    .toString());
        };
    }

    public static ExpectedCondition<Boolean> stableTestabilities() {
        return webDriver -> {
            assert webDriver != null;
            return Boolean.valueOf(((JavascriptExecutor) webDriver)
                    .executeScript("return window.getAllAngularTestabilities().findIndex(x => !x.isStable()) === -1")
                    .toString());
        };
    }
}
