package framework.web.tracing;

import java.util.Objects;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.log.LogLevel;
import org.openqa.selenium.bidi.module.LogInspector;
import org.openqa.selenium.bidi.module.Network;
import org.openqa.selenium.bidi.network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// wip
// does not work with chrome
@Deprecated(forRemoval = false)
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

  private void networkRequests() {
    try (Network network = new Network(this.webDriver)) {
      network.addIntercept(new AddInterceptParameters(InterceptPhase.BEFORE_REQUEST_SENT));
      network.onBeforeRequestSent(
          beforeRequestSent -> {
            String requestId = beforeRequestSent.getRequest().getRequestId();
            LOGGER.info(this.createLogMessage(beforeRequestSent.getRequest()));
            network.continueRequest(new ContinueRequestParameters(requestId));
          });

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
        "Request => method: %s, url: %s", requestData.getMethod(), requestData.getUrl());
  }

  private String createLogMessage(ResponseData responseData) {
    return String.format(
        "Response => url: %s, status code: %s", responseData.getUrl(), responseData.getStatus());
  }
}
