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
import org.openqa.selenium.bidi.log.GenericLogEntry;
import org.openqa.selenium.bidi.log.LogLevel;
import org.openqa.selenium.bidi.module.LogInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Requires LogInspector instance to be set for particular test */
public class LogsAssertionExtension
    implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private static final Logger LOGGER = LoggerFactory.getLogger(LogsAssertionExtension.class);

  private static final Map<String, CacheEntry> CACHE = new ConcurrentHashMap<>();

  @Override
  public void beforeTestExecution(@NonNull ExtensionContext context) {
    CacheEntry cacheEntry = CACHE.get(this.key(context));

    if (cacheEntry == null) {
      return;
    }

    cacheEntry
        .getLogInspector()
        .onConsoleEntry(
            consoleLogEntry -> {
              this.log(consoleLogEntry);
              cacheEntry.getConsoleLogs().add(consoleLogEntry);
            });

    cacheEntry
        .getLogInspector()
        .onJavaScriptLog(
            javascriptLogEntry -> {
              this.log(javascriptLogEntry);
              cacheEntry.getJavascriptLogs().add(javascriptLogEntry);
            });
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
        this.filterErrors(cacheEntry.getConsoleLogs(), cacheEntry.getIgnoringPredicate());

    if (!filteredConsoleLogs.isEmpty()) {
      Assertions.fail("Console log errors have been caught:" + this.toMessage(filteredConsoleLogs));
    }

    List<LogMessage> filteredJavascriptLogs =
        this.filterErrors(cacheEntry.getJavascriptLogs(), cacheEntry.getIgnoringPredicate());

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

  private List<LogMessage> filterErrors(
      List<? extends GenericLogEntry> logEntries, Predicate<LogMessage> predicate) {
    return logEntries.stream()
        .filter(logEntry -> logEntry.getLevel() == LogLevel.ERROR)
        .map(LogMessage::new)
        .filter(Predicate.not(predicate))
        .toList();
  }

  private String toMessage(List<LogMessage> logMessages) {
    return logMessages.stream()
        .map(LogMessage::asString)
        .collect(Collectors.joining(System.lineSeparator()));
  }

  private <T extends GenericLogEntry> void log(T logEntry) {
    LogLevel logLevel = logEntry.getLevel();

    switch (logLevel) {
      case ERROR -> LOGGER.error(new LogMessage(logEntry).asString());
      case WARNING -> LOGGER.warn(new LogMessage(logEntry).asString());
      case INFO -> LOGGER.info(new LogMessage(logEntry).asString());
      default -> LOGGER.debug(new LogMessage(logEntry).asString());
    }
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
