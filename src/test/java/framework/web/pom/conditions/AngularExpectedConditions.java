package framework.web.pom.conditions;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AngularExpectedConditions {

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

  public static ExpectedCondition<Boolean> spinnerToBeGone() {
    return webDriver -> {
      try {
        TimeUnit.MILLISECONDS.sleep(250); // NOSONAR
      } catch (InterruptedException ignored) { // NOSONAR
      }

      Objects.requireNonNull(webDriver);
      List<WebElement> elements = webDriver.findElements(By.xpath("//ngx-spinner/child::*"));

      return ExpectedConditions.invisibilityOfAllElements(elements).apply(webDriver);
    };
  }

  private static String executeScript(String script, WebDriver webDriver) {
    return Objects.requireNonNull(((JavascriptExecutor) webDriver).executeScript(script))
        .toString();
  }
}
