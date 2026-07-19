package com.github.ankowals.framework.web.extensions.logs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.openqa.selenium.bidi.log.ConsoleLogEntry;
import org.openqa.selenium.bidi.log.GenericLogEntry;
import org.openqa.selenium.bidi.log.JavascriptLogEntry;
import org.openqa.selenium.bidi.log.LogLevel;
import org.openqa.selenium.bidi.module.LogInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEntry {

  private static final Logger LOGGER = LoggerFactory.getLogger(CacheEntry.class);

  private final LogInspector logInspector;
  private final List<ConsoleLogEntry> consoleLogs;
  private final List<JavascriptLogEntry> javascriptLogs;
  private Predicate<LogMessage> ignoringPredicate;

  CacheEntry(LogInspector logInspector) {
    this(logInspector, logMessage -> false);
  }

  CacheEntry(LogInspector logIspector, Predicate<LogMessage> predicate) {
    if (logIspector == null) {
      throw new IllegalArgumentException("LogInspector cannot be null");
    }

    this.logInspector = logIspector;
    this.setIgnoringPredicate(predicate);
    this.consoleLogs = new ArrayList<>();
    this.javascriptLogs = new ArrayList<>();

    this.logInspector.onConsoleEntry(
        consoleLogEntry -> {
          this.log(consoleLogEntry);
          this.consoleLogs.add(consoleLogEntry);
        });

    this.logInspector.onJavaScriptLog(
        javascriptLogEntry -> {
          this.log(javascriptLogEntry);
          this.javascriptLogs.add(javascriptLogEntry);
        });
  }

  void closeHandler() {
    this.logInspector.close();
  }

  void clear() {
    this.consoleLogs.clear();
    this.javascriptLogs.clear();
  }

  List<LogMessage> getConsoleLogs() {
    return this.filter(this.consoleLogs, this.ignoringPredicate);
  }

  List<LogMessage> getJavascriptLogs() {
    return this.filter(this.javascriptLogs, this.ignoringPredicate);
  }

  void setIgnoringPredicate(Predicate<LogMessage> predicate) {
    this.ignoringPredicate = Predicate.not(predicate);
  }

  private List<LogMessage> filter(
      List<? extends GenericLogEntry> logEntries, Predicate<LogMessage> predicate) {
    return logEntries.stream()
        .filter(logEntry -> logEntry.getLevel() == LogLevel.ERROR)
        .map(LogMessage::new)
        .filter(predicate)
        .toList();
  }

  private <T extends GenericLogEntry> void log(T logEntry) {
    LogLevel logLevel = logEntry.getLevel();
    String msg = new LogMessage(logEntry).asString();

    switch (logLevel) {
      case ERROR -> LOGGER.error(msg);
      case WARNING -> LOGGER.warn(msg);
      case INFO -> LOGGER.info(msg);
      default -> LOGGER.debug(msg);
    }
  }
}
