package com.github.ankowals.framework.web.assertions.requests;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import org.openqa.selenium.bidi.network.RequestData;
import org.openqa.selenium.bidi.network.ResponseDetails;

public record ResponseLogMessage(LocalDateTime at, String method, String url, int status) {

  ResponseLogMessage(ResponseDetails responseDetails, RequestData requestData) {
    this(
        ResponseLogMessage.extractTimestamp(responseDetails),
        requestData.getMethod(),
        responseDetails.getResponseData().getUrl(),
        responseDetails.getResponseData().getStatus());
  }

  String asString() {
    return "at: %s, method: %s, url: %s, status: %s"
        .formatted(this.at, this.method, this.url, this.status);
  }

  private static LocalDateTime extractTimestamp(ResponseDetails responseDetails) {
    return Instant.ofEpochMilli(responseDetails.getTimestamp())
        .atZone(ZoneId.systemDefault())
        .toLocalDateTime();
  }
}
