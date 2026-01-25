package com.github.ankowals.framework.web.tracing.bidi;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.openqa.selenium.bidi.network.BeforeRequestSent;

record RequestLogMessage(LocalDateTime at, String method, String url) {

  RequestLogMessage(BeforeRequestSent beforeRequestSent) {
    this(
        RequestLogMessage.extractTimestamp(beforeRequestSent),
        beforeRequestSent.getRequest().getMethod(),
        beforeRequestSent.getRequest().getUrl());
  }

  String asString() {
    return "at: %s, method: %s, url: %s".formatted(this.at, this.method, this.url);
  }

  private static LocalDateTime extractTimestamp(BeforeRequestSent beforeRequestSent) {
    return Instant.ofEpochMilli(beforeRequestSent.getTimestamp())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }
}
