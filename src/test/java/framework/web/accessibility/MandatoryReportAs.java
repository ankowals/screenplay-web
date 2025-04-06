package framework.web.accessibility;

import java.io.File;
import org.junit.jupiter.api.TestInfo;

public interface MandatoryReportAs {
  AccessibilityAssertions reportAs(File parentDir, TestInfo testInfo);
}
