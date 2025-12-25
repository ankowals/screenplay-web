package framework.reporting;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.TestIdentifier;

class FqdnTestName {

  private final String fqdnName;
  private final String className;
  private final String methodName;

  FqdnTestName(TestIdentifier testIdentifier) {
    this.fqdnName =
        testIdentifier.getUniqueIdObject().getSegments().stream()
            .skip(1)
            .map(UniqueId.Segment::getValue)
            .collect(Collectors.joining("."));

    this.className = this.toClassName(testIdentifier);
    this.methodName = this.toMethodName(testIdentifier);
  }

  // segments[0] = engine
  // segments[1] = package + class
  // segments[2] = nested class or method
  // etc...
  String asString() {
    return this.fqdnName;
  }

  String getClassName() {
    return this.className;
  }

  String getMethodName() {
    return this.methodName;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || this.getClass() != o.getClass()) return false;
    FqdnTestName that = (FqdnTestName) o;
    return Objects.equals(this.fqdnName, that.fqdnName)
        && Objects.equals(this.className, that.className)
        && Objects.equals(this.methodName, that.methodName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.fqdnName, this.className, this.methodName);
  }
}
