package tests.screenplay;

import java.io.File;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junitpioneer.jupiter.DisableIfTestFails;
import screenplay.task.TaskReportingReportExtension;

@DisableIfTestFails
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ScreenplayTestBase {

  @RegisterExtension
  static final TaskReportingReportExtension EXTENT_REPORT_EXTENSION =
      new TaskReportingReportExtension(
          new File("build/reports/screenplay-extent-report/index.html"));
}
