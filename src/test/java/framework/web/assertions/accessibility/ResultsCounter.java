package framework.web.assertions.accessibility;

import com.deque.html.axecore.results.Results;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class ResultsCounter {

  private final Results results;

  ResultsCounter(Results results) {
    this.results = results;
  }

  int countViolations() {
    List<String> targets = new ArrayList<>();

    this.results
        .getViolations()
        .forEach(
            rule ->
                rule.getNodes()
                    .forEach(
                        checkedNode -> targets.addAll(this.toStringList(checkedNode.getTarget()))));

    return targets.size();
  }

  int countPasses() {
    List<String> targets = new ArrayList<>();

    this.results
        .getPasses()
        .forEach(
            rule ->
                rule.getNodes()
                    .forEach(
                        checkedNode -> targets.addAll(this.toStringList(checkedNode.getTarget()))));

    return targets.size();
  }

  int countInapplicable() {
    return this.results.getInapplicable().size();
  }

  int countIncomplete() {
    return this.results.getIncomplete().size();
  }

  private List<String> toStringList(Object obj) {
    return obj == null
        ? null
        : (obj instanceof String
            ? List.of((String) obj)
            : (obj.getClass().isArray()
                    ? Arrays.asList((Object[]) obj)
                    : (obj instanceof Collection ? ((Collection<?>) obj) : List.of()))
                .stream().map(String::valueOf).toList());
  }
}
