package com.github.ankowals.framework.web.assertions.logs;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.openqa.selenium.bidi.log.ConsoleLogEntry;
import org.openqa.selenium.bidi.log.JavascriptLogEntry;
import org.openqa.selenium.bidi.module.LogInspector;

public class CacheEntry {

  private final LogInspector logInspector;
  private final List<ConsoleLogEntry> consoleLogs;
  private final List<JavascriptLogEntry> javascriptLogs;
  private Predicate<LogMessage> ignoringPredicate;

  CacheEntry(LogInspector logInspector) {
    this(logInspector, logMessage -> false);
  }

  CacheEntry(LogInspector logIspector, Predicate<LogMessage> ignoringPredicate) {
    if (logIspector == null) {
      throw new IllegalArgumentException("LogInspector cannot be null");
    }

    this.logInspector = logIspector;
    this.ignoringPredicate = ignoringPredicate;
    this.consoleLogs = new ArrayList<>();
    this.javascriptLogs = new ArrayList<>();
  }

  void clear() {
    this.consoleLogs.clear();
    this.javascriptLogs.clear();
  }

  LogInspector getLogInspector() {
    return logInspector;
  }

  List<ConsoleLogEntry> getConsoleLogs() {
    return consoleLogs;
  }

  List<JavascriptLogEntry> getJavascriptLogs() {
    return javascriptLogs;
  }

  Predicate<LogMessage> getIgnoringPredicate() {
    return ignoringPredicate;
  }

  void setIgnoringPredicate(Predicate<LogMessage> ignoringPredicate) {
    this.ignoringPredicate = ignoringPredicate;
  }
}
