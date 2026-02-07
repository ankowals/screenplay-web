package com.github.ankowals.framework.web.assertions.requests;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.openqa.selenium.bidi.network.BeforeRequestSent;
import org.openqa.selenium.bidi.network.RequestData;

public record RequestLogMessage(LocalDateTime at, String method, String url) {

  RequestLogMessage(RequestData requestData) {
    this(
        RequestLogMessage.toLocalDateTime(
            ((Number) requestData.getTimings().getRequestStart()).longValue()),
        requestData.getMethod(),
        requestData.getUrl());
  }

  RequestLogMessage(BeforeRequestSent beforeRequestSent) {
    this(
        RequestLogMessage.toLocalDateTime(beforeRequestSent.getTimestamp()),
        beforeRequestSent.getRequest().getMethod(),
        beforeRequestSent.getRequest().getUrl());
  }

  String asString() {
    return "at: %s, method: %s, url: %s".formatted(this.at, this.method, this.url);
  }

  private static LocalDateTime toLocalDateTime(long timestamp) {
    return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
  }
}
