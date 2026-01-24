package com.github.ankowals.framework.web.assertions.logs;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.bidi.log.GenericLogEntry;
import org.openqa.selenium.bidi.log.StackFrame;
import org.openqa.selenium.bidi.log.StackTrace;

public record LogMessage(LocalDateTime at, String text, String stackTrace) {

  LogMessage(GenericLogEntry genericLogEntry) {
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
