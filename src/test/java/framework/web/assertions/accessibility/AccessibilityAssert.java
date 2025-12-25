package framework.web.assertions.accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.AxeBuilder;
import java.io.File;
import java.util.function.Consumer;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;

public class AccessibilityAssert implements MandatoryReportAs {

  private final WebDriver webDriver;
  private final AxeBuilder axeBuilder;

  private String title;
  private File destination;

  private AccessibilityAssert(WebDriver webDriver, AxeBuilder axeBuilder) {
    this.webDriver = webDriver;
    this.axeBuilder = axeBuilder;
  }

  public static MandatoryReportAs assertThatPage(WebDriver webDriver) {
    return new AccessibilityAssert(webDriver, new AxeBuilder());
  }

  @Override
  public AccessibilityAssert reportAs(File destination, String title) {
    this.destination = destination;
    this.title = title;
    return this;
  }

  public void isViolationFree() {
    if (!Boolean.parseBoolean(System.getenv("ACCESSIBILITY_ASSERTIONS_ENABLED"))) {
      return;
    }

    Results results = this.axeBuilder.analyze(this.webDriver);

    new HtmlReportCreator(results).create(this.destination.getAbsolutePath(), this.title);

    Assertions.assertThat(results.violationFree())
        .withFailMessage(
            "Accessibility report %s validation failed!", this.destination.getAbsolutePath())
        .isTrue();
  }

  public AccessibilityAssert customizeAxe(Consumer<AxeBuilder> customizer) {
    customizer.accept(this.axeBuilder);
    return this;
  }
}
