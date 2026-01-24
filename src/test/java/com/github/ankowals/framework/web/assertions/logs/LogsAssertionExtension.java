package com.github.ankowals.framework.web.assertions.logs;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.bidi.log.FilterBy;
import org.openqa.selenium.bidi.log.GenericLogEntry;
import org.openqa.selenium.bidi.log.LogLevel;
import org.openqa.selenium.bidi.module.LogInspector;

/** Requires LogInspector instance to be set for particular test */
public class LogsAssertionExtension
    implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private static final Map<String, CacheEntry> CACHE = new ConcurrentHashMap<>();

  @Override
  public void beforeTestExecution(@NonNull ExtensionContext context) {
    CacheEntry cacheEntry = CACHE.get(this.key(context));

    if (cacheEntry == null) {
      return;
    }

    cacheEntry
        .getLogInspector()
        .onConsoleEntry(cacheEntry.getConsoleLogs()::add, FilterBy.logLevel(LogLevel.ERROR));
    cacheEntry.getLogInspector().onJavaScriptException(cacheEntry.getJavascriptLogs()::add);
  }

  @Override
  public void afterTestExecution(@NonNull ExtensionContext context) {
    String key = this.key(context);
    CacheEntry cacheEntry = CACHE.get(key);

    if (cacheEntry == null) {
      return;
    }

    cacheEntry.getLogInspector().close();

    List<LogMessage> filteredConsoleLogs =
        this.filter(cacheEntry.getConsoleLogs(), cacheEntry.getIgnoringPredicate());

    List<LogMessage> filteredJavascriptLogs =
        this.filter(cacheEntry.getJavascriptLogs(), cacheEntry.getIgnoringPredicate());

    if (!filteredConsoleLogs.isEmpty()) {
      Assertions.fail("Console log errors have been caught:" + this.toMessage(filteredConsoleLogs));
    }

    if (!filteredJavascriptLogs.isEmpty()) {
      Assertions.fail(
          "Javascript exceptions have been caught:" + this.toMessage(filteredJavascriptLogs));
    }

    cacheEntry.clear();
    CACHE.remove(key);
  }

  public void logInspector(LogInspector logInspector, TestInfo testInfo) {
    CACHE.put(this.key(testInfo), new CacheEntry(logInspector));
  }

  public void ignoringPredicate(Predicate<LogMessage> predicate, TestInfo testInfo) {
    CacheEntry cacheEntry = CACHE.get(this.key(testInfo));

    if (cacheEntry != null) {
      cacheEntry.setIgnoringPredicate(predicate);
    }
  }

  private List<LogMessage> filter(
      List<? extends GenericLogEntry> logEntries, Predicate<LogMessage> predicate) {
    return logEntries.stream().map(LogMessage::new).filter(Predicate.not(predicate)).toList();
  }

  private String toMessage(List<LogMessage> logMessages) {
    return logMessages.stream()
        .map(LogMessage::asString)
        .collect(Collectors.joining(System.lineSeparator()));
  }

  private String key(ExtensionContext context) {
    return this.key(context.getTestClass().orElseThrow(), context.getDisplayName());
  }

  private String key(TestInfo testInfo) {
    return this.key(testInfo.getTestClass().orElseThrow(), testInfo.getDisplayName());
  }

  private String key(Class<?> testClass, String displayName) {
    return "%s.%s".formatted(testClass.getName(), displayName);
  }
}
