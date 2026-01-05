package domain.saucedemo.questions;

import framework.reporting.ExtentWebReportExtension;
import framework.screenplay.Question;
import framework.screenplay.actor.use.UseAbility;
import framework.web.screenplay.BrowseTheWeb;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class TheScreenshot {
  public static Question<File> takenFor(TestInfo testInfo) {
    return actor -> {
      WebDriver webDriver = UseAbility.of(actor).to(BrowseTheWeb.class).driver();
      byte[] bytes = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);

      String name =
          "%s/%s-%s.%s"
              .formatted(
                  testInfo.getTestClass().orElseThrow().getName(),
                  testInfo.getTestMethod().orElseThrow().getName(),
                  UUID.randomUUID(),
                  "png");

      File file = Path.of(ExtentWebReportExtension.REPORT_FILE.getParent(), name).toFile();
      Files.write(file.toPath(), bytes);

      return file;
    };
  }
}
