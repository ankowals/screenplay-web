package com.github.ankowals.framework.web.extensions.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.openqa.selenium.bidi.module.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheEntry {

  private static final Logger LOGGER = LoggerFactory.getLogger(CacheEntry.class);

  private final Network network;
  private final List<ResponseLogMessage> responseLogMessages;
  private Predicate<ResponseLogMessage> ignoringPredicate;

  CacheEntry(Network network) {
    this(network, response -> false);
  }

  CacheEntry(Network network, Predicate<ResponseLogMessage> predicate) {
    if (network == null) {
      throw new IllegalArgumentException("Network cannot be null");
    }

    this.network = network;
    this.setIgnoringPredicate(predicate);
    this.responseLogMessages = new ArrayList<>();

    this.network.onResponseStarted(
        response -> {
          ResponseLogMessage responseLogMessage =
              new ResponseLogMessage(response, response.getRequest());

          if (responseLogMessage.status() > 399) {
            LOGGER.error(responseLogMessage.asString());
            this.responseLogMessages.add(responseLogMessage);
          }
        });
  }

  void closeHandler() {
    this.network.close();
  }

  void clear() {
    this.responseLogMessages.clear();
  }

  List<ResponseLogMessage> getResponseLogMessages() {
    return this.responseLogMessages.stream().filter(this.ignoringPredicate).toList();
  }

  void setIgnoringPredicate(Predicate<ResponseLogMessage> predicate) {
    this.ignoringPredicate = Predicate.not(predicate);
  }
}
