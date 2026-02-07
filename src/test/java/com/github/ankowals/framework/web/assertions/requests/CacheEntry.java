package com.github.ankowals.framework.web.assertions.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.openqa.selenium.bidi.module.Network;

public class CacheEntry {

  private final Network network;
  private final List<ResponseLogMessage> responseLogMessages;
  private Predicate<ResponseLogMessage> ignoringPredicate;

  CacheEntry(Network network) {
    this(network, response -> false);
  }

  CacheEntry(Network network, Predicate<ResponseLogMessage> ignoringPredicate) {
    if (network == null) {
      throw new IllegalArgumentException("Network cannot be null");
    }

    this.network = network;
    this.ignoringPredicate = ignoringPredicate;
    this.responseLogMessages = new ArrayList<>();
  }

  void clear() {
    this.responseLogMessages.clear();
  }

  Network getNetwork() {
    return network;
  }

  List<ResponseLogMessage> getResponseLogMessages() {
    return this.responseLogMessages;
  }

  Predicate<ResponseLogMessage> getIgnoringPredicate() {
    return this.ignoringPredicate;
  }

  void setIgnoringPredicate(Predicate<ResponseLogMessage> ignoringPredicate) {
    this.ignoringPredicate = ignoringPredicate;
  }
}
