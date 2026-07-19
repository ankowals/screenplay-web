package com.github.ankowals.framework.web.extensions.logs;

import com.github.ankowals.framework.web.extensions.CacheKey;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.bidi.module.LogInspector;

/** Requires LogInspector instance to be set for particular test */
public class LogsAssertionExtension implements AfterTestExecutionCallback {

  private static final Map<String, CacheEntry> CACHE = new ConcurrentHashMap<>();

  public void addOnLogHandler(LogInspector logInspector, TestInfo testInfo) {
    CACHE.put(CacheKey.from(testInfo), new CacheEntry(logInspector));
  }

  public void ignoringPredicate(Predicate<LogMessage> predicate, TestInfo testInfo) {
    CacheEntry cacheEntry = CACHE.get(CacheKey.from(testInfo));

    if (cacheEntry != null) {
      cacheEntry.setIgnoringPredicate(predicate);
    }
  }

  @Override
  public void afterTestExecution(@NonNull ExtensionContext extensionContext) {
    String key = CacheKey.from(extensionContext);
    CacheEntry cacheEntry = CACHE.get(key);

    if (cacheEntry == null) {
      return;
    }

    cacheEntry.closeHandler();

    List<LogMessage> filteredConsoleLogs = cacheEntry.getConsoleLogs();

    if (!filteredConsoleLogs.isEmpty()) {
      Assertions.fail("Console log errors have been caught:" + this.toMessage(filteredConsoleLogs));
    }

    List<LogMessage> filteredJavascriptLogs = cacheEntry.getJavascriptLogs();

    if (!filteredJavascriptLogs.isEmpty()) {
      Assertions.fail(
          "Javascript exceptions have been caught:" + this.toMessage(filteredJavascriptLogs));
    }

    cacheEntry.clear();
    CACHE.remove(key);
  }

  private String toMessage(List<LogMessage> logMessages) {
    return logMessages.stream()
        .map(LogMessage::asString)
        .collect(Collectors.joining(System.lineSeparator()));
  }
}
