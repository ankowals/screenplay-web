package framework.web.tracing;

import framework.web.reporting.ExtentWebReportExtension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v142.log.Log;
import org.openqa.selenium.devtools.v142.log.model.LogEntry;
import org.openqa.selenium.devtools.v142.network.Network;
import org.openqa.selenium.devtools.v142.network.model.*;
import org.openqa.selenium.devtools.v142.page.Page;
import org.openqa.selenium.devtools.v142.page.model.ScreencastFrameMetadata;
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

  public ListenerRegistrar addPageScreencastListener() {
    this.devTools.addListener(
        Page.screencastFrame(),
        screencastFrame -> {
          int sessionId = screencastFrame.getSessionId();
          ScreencastFrameMetadata screencastFrameMetadata = screencastFrame.getMetadata();
          String data = screencastFrame.getData();
          byte[] bytes = Base64.getDecoder().decode(data);

          try {
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
            String name =
                String.format(
                    "%s_%s.png", sessionId, screencastFrameMetadata.getTimestamp().orElseThrow());
            File file = Path.of(ExtentWebReportExtension.REPORT_FILE.getParent(), name).toFile();
            ImageIO.write(img, "png", file);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        });

    return this;
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
    } catch (WebDriverException ignored) { // NOSONAR
      return "";
    }
  }
}
