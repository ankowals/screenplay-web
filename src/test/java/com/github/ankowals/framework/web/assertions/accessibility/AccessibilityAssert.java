package com.github.ankowals.framework.web.assertions.accessibility;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.selenium.AxeBuilder;
import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;
import org.apache.commons.io.FilenameUtils;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.WebDriver;

public class AccessibilityAssert implements MandatoryReportAs {

  private final WebDriver webDriver;
  private final AxeBuilder axeBuilder;

  private File destination;

  private AccessibilityAssert(WebDriver webDriver, AxeBuilder axeBuilder) {
    this.webDriver = webDriver;
    this.axeBuilder = axeBuilder;
  }

  public static MandatoryReportAs assertThatPage(WebDriver webDriver) {
    return new AccessibilityAssert(webDriver, new AxeBuilder());
  }

  @Override
  public AccessibilityAssert reportAs(Path destination) {
    this.destination = destination.toFile();
    return this;
  }

  public void isViolationFree() {
    if (!Boolean.parseBoolean(System.getenv("ACCESSIBILITY_ASSERTIONS_ENABLED"))) {
      return;
    }

    Results results = this.axeBuilder.analyze(this.webDriver);

    String path = this.destination.getAbsolutePath();
    String fileName = FilenameUtils.getName(path);

    new HtmlReportCreator(results).create(path, FilenameUtils.getBaseName(fileName));

    Assertions.assertThat(results.violationFree())
        .withFailMessage("Accessibility report %s validation failed!", path)
        .isTrue();
  }

  public AccessibilityAssert customizeAxe(Consumer<AxeBuilder> customizer) {
    customizer.accept(this.axeBuilder);
    return this;
  }
}
