package com.github.ankowals.framework.web.tracing.bidi;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.openqa.selenium.bidi.network.ResponseDetails;

record ResponseLogMessage(LocalDateTime at, String method, String url) {

  ResponseLogMessage(ResponseDetails responseDetails) {
    this(
        ResponseLogMessage.extractTimestamp(responseDetails),
        responseDetails.getResponseData().getUrl(),
        String.valueOf(responseDetails.getResponseData().getStatus()));
  }

  String asString() {
    return "at: %s, url: %s, status: %s".formatted(this.at, this.method, this.url);
  }

  private static LocalDateTime extractTimestamp(ResponseDetails responseDetails) {
    return Instant.ofEpochMilli(responseDetails.getTimestamp())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }
}
