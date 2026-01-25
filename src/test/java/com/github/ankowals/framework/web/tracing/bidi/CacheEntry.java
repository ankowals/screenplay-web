package com.github.ankowals.framework.web.tracing.bidi;

import org.openqa.selenium.bidi.module.Network;

class CacheEntry {

  private final Network network;

  CacheEntry(Network network) {
    if (network == null) {
      throw new IllegalArgumentException("Network cannot be null");
    }

    this.network = network;
  }

  Network getNetwork() {
    return network;
  }
}
