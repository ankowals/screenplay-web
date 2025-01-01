package framework.web.tracing;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.log.LogLevel;
import org.openqa.selenium.bidi.module.LogInspector;
import org.openqa.selenium.bidi.module.Network;
import org.openqa.selenium.bidi.network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BiDiTracer {

  private static final Logger LOGGER = LoggerFactory.getLogger(BiDiTracer.class);

  private final WebDriver webDriver;

  public BiDiTracer(WebDriver webDriver) {
    this.webDriver = webDriver;
  }

  public void trace() {
    this.consoleLogs();
    this.networkRequests();
  }

  // request data is missing at the moment
  // how to filter request triggered by different api calls? like fetch only??
  // response content contains only response size at the moment
  private void networkRequests() {
    try (Network network = new Network(this.webDriver)) {
      network.addIntercept(new AddInterceptParameters(InterceptPhase.BEFORE_REQUEST_SENT));
      network.onBeforeRequestSent(event -> LOGGER.info(this.createLogMessage(event.getRequest())));
      network.onResponseCompleted(
          responseDetails -> LOGGER.info(this.createLogMessage(responseDetails.getResponseData())));
    }
  }

  private void consoleLogs() {
    try (LogInspector logInspector = new LogInspector(this.webDriver)) {
      logInspector.onConsoleEntry(
          entry -> {
            if (entry.getLevel().equals(LogLevel.ERROR)) {
              LOGGER.error(
                  "Console log entry: {}{}{}",
                  entry.getText(),
                  System.lineSeparator(),
                  Objects.toString(entry.getStackTrace(), ""));
            }
          });

      logInspector.onJavaScriptException(
          entry -> {
            LOGGER.error("Java script exception: {}", entry.getText());
            entry.getStackTrace().getCallFrames().forEach(frame -> LOGGER.error(frame.toString()));
          });
    }
  }

  private String createLogMessage(RequestData requestData) {
    return String.format(
        "Request => method: %s, url: %s%s%s%s%s",
        requestData.getMethod(),
        requestData.getUrl(),
        System.lineSeparator(),
        this.convertToString(requestData.getHeaders()),
        System.lineSeparator(),
        System.lineSeparator());
  }

  private String createLogMessage(ResponseData responseData) {
    return String.format(
        "Response => url: %s, status code: %s%s%s%s%s%s",
        responseData.getUrl(),
        responseData.getStatus(),
        System.lineSeparator(),
        this.convertToString(responseData.getHeaders()),
        System.lineSeparator(),
        System.lineSeparator(),
        responseData.getContent().orElse(0L));
  }

  private String convertToString(List<Header> headers) {
    return headers.stream()
        .map(Header::toMap)
        // .map(Map::entrySet)
        .map(
            map ->
                map.keySet().stream()
                    .map(key -> key + "=" + map.get(key))
                    .collect(Collectors.joining(", ", "{", "}")))
        // .map(map -> String.format("%s: %s", e.getKey(), e.getValue()))
        .collect(Collectors.joining(System.lineSeparator()));
  }
}
