package com.github.ankowals.framework.reporting;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Strings;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.TestIdentifier;

class FqdnTestName {

  private final String fullTestName;
  private final String className;
  private final String methodName;
  private final int invocationCount;

  FqdnTestName(TestIdentifier testIdentifier) {
    this.fullTestName = this.toFullTestName(testIdentifier);
    this.className = this.toClassName(testIdentifier);
    this.methodName = this.toMethodName(testIdentifier);
    this.invocationCount = this.toInvocationCount(testIdentifier);
  }

  // segments[0] = engine
  // segments[1] = package + class
  // segments[2] = nested class or method
  // etc...
  String getFullTestName() {
    return this.fullTestName;
  }

  String getTestName() {
    return "%s.%s[%s]".formatted(this.getClassName(), this.getMethodName(), this.invocationCount);
  }

  String getClassName() {
    return this.className;
  }

  String getMethodName() {
    return this.methodName;
  }

  String getMethodNameWithInvocationCount() {
    return "%s[%s]".formatted(this.getMethodName(), this.invocationCount);
  }

  private String toFullTestName(TestIdentifier testIdentifier) {
    return testIdentifier.getUniqueIdObject().getSegments().stream()
        .skip(1)
        .map(UniqueId.Segment::getValue)
        .collect(Collectors.joining("."));
  }

  private String toClassName(TestIdentifier testIdentifier) {
    Optional<String> maybeClassSegment =
        testIdentifier.getUniqueIdObject().getSegments().stream()
            .filter(
                segment ->
                    segment.getType().equals("class") || segment.getType().equals("nested-class"))
            .map(UniqueId.Segment::getValue)
            .findFirst();

    return maybeClassSegment.orElseThrow();
  }

  private String toMethodName(TestIdentifier testIdentifier) {
    Optional<String> maybeMethodSegment =
        testIdentifier.getUniqueIdObject().getSegments().stream()
            .filter(
                segment ->
                    segment.getType().equals("method")
                        || segment.getType().equals("test-template")
                        || segment.getType().equals("dynamic-test"))
            .map(UniqueId.Segment::getValue)
            .findFirst();

    String methodSegment = maybeMethodSegment.orElseThrow();
    return methodSegment.substring(0, methodSegment.indexOf("("));
  }

  // if argument provider used it will look like #1, #2 etc.
  private int toInvocationCount(TestIdentifier testIdentifier) {
    String lastSegment =
        testIdentifier.getUniqueIdObject().getSegments().stream()
            .skip(1)
            .map(UniqueId.Segment::getValue)
            .toList()
            .getLast();

    String valueToParse = StringUtils.substringAfterLast(lastSegment, "#");

    if (Strings.isNullOrEmpty(valueToParse)) {
      return 1;
    }

    return Integer.parseInt(valueToParse);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    FqdnTestName that = (FqdnTestName) o;
    return Objects.equals(this.fullTestName, that.fullTestName)
        && Objects.equals(this.className, that.className)
        && Objects.equals(this.methodName, that.methodName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.fullTestName, this.className, this.methodName);
  }
}
