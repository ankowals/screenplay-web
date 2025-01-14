package framework.screenplay.helpers.task;

import org.junit.jupiter.api.TestInfo;

public record TestIdentifier(String className, String methodName) {
  public TestIdentifier(TestInfo testInfo) {
    this(
        testInfo.getTestClass().orElseThrow().getName(),
        testInfo.getTestMethod().orElseThrow().getName());
  }
}
