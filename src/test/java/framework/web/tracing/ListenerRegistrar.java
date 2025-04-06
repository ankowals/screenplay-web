package framework.web.tracing;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v134.log.Log;
import org.openqa.selenium.devtools.v134.log.model.LogEntry;
import org.openqa.selenium.devtools.v134.network.Network;
import org.openqa.selenium.devtools.v134.network.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
Support may vary between different drivers
 */
public class ListenerRegistrar {

  private static final Logger LOGGER = LoggerFactory.getLogger(ListenerRegistrar.class);

  private final DevTools devTools;

  ListenerRegistrar(DevTools devTools) {
    this.devTools = devTools;
  }

  public ListenerRegistrar addNetworkRequestListener() {
    this.devTools.addListener(
        Network.requestWillBeSent(),
        entry -> {
          Request request = entry.getRequest();
          if (entry.getType().equals(Optional.of(ResourceType.FETCH))) {
            LOGGER.info(this.createLogMessage(request));
          }
        });

    return this;
  }

  public ListenerRegistrar addNetworkResponseListener() {
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

    return this;
  }

  public ListenerRegistrar addConsoleLogListener() {
    this.devTools.addListener(
        Log.entryAdded(),
        entry -> {
          if (entry.getLevel().equals(LogEntry.Level.ERROR)) {
            LOGGER.error(
                "Console log entry: {}{}{}",
                entry.getText(),
                System.lineSeparator(),
                Objects.toString(entry.getStackTrace().orElse(null), ""));
          }
        });

    return this;
  }

  public ListenerRegistrar addJavascriptExceptionListener() {
    this.devTools
        .getDomains()
        .events()
        .addJavascriptExceptionListener(
            e -> {
              LOGGER.error("Java script exception: {}", e.getMessage());
              e.printStackTrace();
            });

    return this;
  }

  private String createLogMessage(Request request) {
    return String.format(
        "Request => method: %s, url: %s%s%s%s%s%s",
        request.getMethod(),
        request.getUrl(),
        System.lineSeparator(),
        this.convertToString(request.getHeaders()),
        System.lineSeparator(),
        System.lineSeparator(),
        request.getPostData().orElse(""));
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
        this.getBody(requestId));
  }

  private String convertToString(Headers headers) {
    return headers.toJson().entrySet().stream()
        .map(e -> String.format("%s: %s", e.getKey(), e.getValue()))
        .collect(Collectors.joining(System.lineSeparator()));
  }

  private String getBody(RequestId requestId) {
    try {
      return this.devTools.send(Network.getResponseBody(requestId)).getBody();
    } catch (WebDriverException ignored) {
      return "";
    }
  }
}
