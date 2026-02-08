package com.github.ankowals.abilities;

import com.github.ankowals.framework.screenplay.Ability;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.bidi.log.*;
import org.openqa.selenium.bidi.module.LogInspector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record AccessBrowserLogs(
    LogInspector logInspector,
    List<ConsoleLogEntry> consoleLogs,
    List<JavascriptLogEntry> javascriptLogs)
    implements Ability {

  private static final Logger LOGGER = LoggerFactory.getLogger(AccessBrowserLogs.class);

  public AccessBrowserLogs {
    if (logInspector == null) {
      throw new IllegalArgumentException("LogInspector cannot be null");
    }

    logInspector.onConsoleEntry(
        consoleLogEntry -> {
          this.log(consoleLogEntry);
          consoleLogs.add(consoleLogEntry);
        });

    logInspector.onJavaScriptLog(
        javascriptLogEntry -> {
          this.log(javascriptLogEntry);
          javascriptLogs.add(javascriptLogEntry);
        });
  }

  public static AccessBrowserLogs with(LogInspector logInspector) {
    return new AccessBrowserLogs(logInspector, new ArrayList<>(), new ArrayList<>());
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

  private record LogMessage(LocalDateTime at, String text, String stackTrace) {

    private LogMessage(GenericLogEntry genericLogEntry) {
      this(
          LogMessage.extractTimestamp(genericLogEntry),
          genericLogEntry.getText(),
          LogMessage.extractStackTrace(genericLogEntry));
    }

    String asString() {
      return "at: %s, text: %s, stackTrace: %s%s"
          .formatted(this.at, this.text, System.lineSeparator(), this.stackTrace);
    }

    private static LocalDateTime extractTimestamp(GenericLogEntry genericLogEntry) {
      return Instant.ofEpochMilli(genericLogEntry.getTimestamp())
          .atZone(ZoneId.systemDefault())
          .toLocalDateTime();
    }

    private static String extractStackTrace(GenericLogEntry genericLogEntry) {
      StackTrace stackTrace = genericLogEntry.getStackTrace();

      List<StackFrame> callFrames;

      if (stackTrace != null) {
        callFrames = genericLogEntry.getStackTrace().getCallFrames();
      } else {
        callFrames = Collections.emptyList();
      }

      return callFrames.stream()
          .map(
              stackFrame ->
                  "url: %s, functionName: %s, lineNumber: %s, columnNumber: %s"
                      .formatted(
                          stackFrame.getUrl(),
                          stackFrame.getFunctionName(),
                          stackFrame.getLineNumber(),
                          stackFrame.getColumnNumber()))
          .collect(Collectors.joining(System.lineSeparator()));
    }
  }
}
