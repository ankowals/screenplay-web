package tests.screenplay;

import framework.screenplay.helpers.task.Task;
import framework.screenplay.helpers.task.TestIdentifier;
import java.io.File;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junitpioneer.jupiter.DisableIfTestFails;
import screenplay.task.TaskReportingReportExtension;

@DisableIfTestFails
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ScreenplayTestBase {

  protected Task task;

  @RegisterExtension
  private static final TaskReportingReportExtension EXTENT_REPORT_EXTENSION =
      new TaskReportingReportExtension(
          new File("build/reports/screenplay-extent-report/index.html"));

  @BeforeAll
  void screenplayTestBaseBeforeAll() {
    this.task = new Task(EXTENT_REPORT_EXTENSION);
  }

  protected TestIdentifier toTestIdentifier(TestInfo testInfo) {
    return new TestIdentifier(
        testInfo.getTestClass().orElseThrow().getName(),
        testInfo.getTestMethod().orElseThrow().getName());
  }
}
