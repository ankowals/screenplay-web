package framework.web.reporting;

import org.junit.platform.engine.UniqueId;
import org.junit.platform.launcher.TestIdentifier;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class FqdnTestName {

    private final String fqdnName;
    private final String className;
    private final String methodName;

    FqdnTestName(TestIdentifier testIdentifier) {
        this.fqdnName = testIdentifier.getUniqueIdObject()
                .getSegments()
                .stream().skip(1)
                .map(UniqueId.Segment::getValue)
                .collect(Collectors.joining("."));

        this.className = this.toClassName(testIdentifier);
        this.methodName = this.toMethodName(testIdentifier);
    }

    //segments[0] = engine
    //segments[1] = package + class
    //segments[2] = nested class or method
    //etc...
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
        List<UniqueId.Segment> segments = testIdentifier.getUniqueIdObject().getSegments();
        return segments.get(segments.size() - 2).getValue();
    }

    private String toMethodName(TestIdentifier testIdentifier) {
        String method = testIdentifier.getUniqueIdObject().getLastSegment().getValue();
        return method.substring(0, method.indexOf("("));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FqdnTestName that = (FqdnTestName) o;
        return Objects.equals(fqdnName, that.fqdnName) && Objects.equals(className, that.className) && Objects.equals(methodName, that.methodName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fqdnName, className, methodName);
    }
}
