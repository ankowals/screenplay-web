package com.github.ankowals.framework.web.devtools;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v145.network.Network;
import org.openqa.selenium.devtools.v145.network.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkDomain {

  private static final Logger LOGGER = LoggerFactory.getLogger(NetworkDomain.class);

  private final DevTools devTools;

  NetworkDomain(DevTools devTools) {
    this.devTools = devTools;
  }

  public void enable() {
    this.devTools.send(
        Network.enable(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()));
  }

  public void addRequestListener() {
    this.devTools.addListener(
        Network.requestWillBeSent(),
        entry -> {
          Request request = entry.getRequest();
          if (entry.getType().equals(Optional.of(ResourceType.FETCH))) {
            LOGGER.info(this.createLogMessage(request));
          }
        });
  }

  public void addResponseListener() {
    this.devTools.addListener(
        Network.responseReceived(),
        entry -> {
          Response response = entry.getResponse();
          RequestId requestId = entry.getRequestId();
          if (entry.getType().equals(ResourceType.FETCH)
              || entry.getType().equals(ResourceType.XHR)) {
            if (response.getStatus() >= 400) {
              LOGGER.error(this.createLogMessage(response, requestId));
            } else {
              LOGGER.info(this.createLogMessage(response, requestId));
            }
          }
        });
  }

  public List<Cookie> getCookies() {
    // alternative Storage.getCookies(Optional.empty());
    return this.devTools.send(Network.getAllCookies());
  }

  public void set(List<Cookie> cookies) {
    List<CookieParam> cookieParams =
        cookies.stream()
            .map(
                cookie ->
                    new CookieParam(
                        cookie.getName(),
                        cookie.getValue(),
                        Optional.empty(),
                        Optional.of(cookie.getDomain()),
                        Optional.of(cookie.getPath()),
                        Optional.of(cookie.getSecure()),
                        Optional.of(cookie.getHttpOnly()),
                        cookie.getSameSite(),
                        Optional.of(new TimeSinceEpoch(cookie.getExpires())),
                        Optional.of(cookie.getPriority()),
                        Optional.of(cookie.getSameParty()),
                        Optional.of(cookie.getSourceScheme()),
                        Optional.of(cookie.getSourcePort()),
                        cookie.getPartitionKey()))
            .toList();

    // alternative Storage.setCookies(cookieParams);
    // this.devTools.send(Network.clearBrowserCookies());
    this.devTools.send(Network.setCookies(cookieParams));
  }

  private String createLogMessage(Request request) {
    StringBuilder stringBuilder = new StringBuilder();

    Optional<List<PostDataEntry>> maybePostDataEntries = request.getPostDataEntries();

    maybePostDataEntries.ifPresent(
        postDataEntries ->
            postDataEntries.forEach(
                postDataEntry -> {
                  Optional<String> maybeData = postDataEntry.getBytes();
                  maybeData.ifPresent(stringBuilder::append);
                }));

    String postData = new String(Base64.getDecoder().decode(stringBuilder.toString()));

    return String.format(
        "Request => method: %s, url: %s%s%s%s%s%s",
        request.getMethod(),
        request.getUrl(),
        System.lineSeparator(),
        this.convertToString(request.getHeaders()),
        System.lineSeparator(),
        System.lineSeparator(),
        // request.getPostData().orElse("")
        postData);
  }

  private String createLogMessage(Response response, RequestId requestId) {
    return String.format(
        "Response => url: %s, status code: %s%s%s%s%s%s",
        response.getUrl(),
        response.getStatus(),
        System.lineSeparator(),
        this.convertToString(response.getHeaders()),
        System.lineSeparator(),
        System.lineSeparator(),
        this.getResponseBody(requestId));
  }

  private String convertToString(Headers headers) {
    return headers.toJson().entrySet().stream()
        .map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
        .collect(Collectors.joining(System.lineSeparator()));
  }

  private String getResponseBody(RequestId requestId) {
    try {
      return this.devTools.send(Network.getResponseBody(requestId)).getBody();
    } catch (WebDriverException ignored) { // NOSONAR
      return "";
    }
  }
}
