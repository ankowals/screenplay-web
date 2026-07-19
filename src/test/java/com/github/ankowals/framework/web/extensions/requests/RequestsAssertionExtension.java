package com.github.ankowals.framework.web.extensions.requests;

import com.github.ankowals.framework.web.extensions.CacheKey;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.bidi.module.Network;

// support for data retrieval will be provided in https://github.com/SeleniumHQ/selenium/pull/16336

/** Requires Network instance to be set for particular test */
public class RequestsAssertionExtension implements AfterTestExecutionCallback {

  private static final Map<String, CacheEntry> CACHE = new ConcurrentHashMap<>();

  public void addOnResponseHandler(Network network, TestInfo testInfo) {
    CACHE.put(CacheKey.from(testInfo), new CacheEntry(network));
  }

  public void ignoringPredicate(Predicate<ResponseLogMessage> predicate, TestInfo testInfo) {
    CacheEntry cacheEntry = CACHE.get(CacheKey.from(testInfo));

    if (cacheEntry != null) {
      cacheEntry.setIgnoringPredicate(predicate);
    }
  }

  @Override
  public void afterTestExecution(@NonNull ExtensionContext extensionContext) {
    String key = CacheKey.from(extensionContext);
    CacheEntry cacheEntry = CACHE.get(key);

    if (cacheEntry == null) {
      return;
    }

    cacheEntry.closeHandler();

    List<ResponseLogMessage> filteredResponseLogMessages = cacheEntry.getResponseLogMessages();

    if (!filteredResponseLogMessages.isEmpty()) {
      Assertions.fail(
          "Failed request have been caught:" + this.toMessage(filteredResponseLogMessages));
    }

    cacheEntry.clear();
    CACHE.remove(key);
  }

  private String toMessage(List<ResponseLogMessage> logMessages) {
    return logMessages.stream()
        .map(ResponseLogMessage::asString)
        .collect(Collectors.joining(System.lineSeparator()));
  }
}
