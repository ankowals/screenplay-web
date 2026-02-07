package com.github.ankowals.framework.web.assertions.requests;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.bidi.module.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// support for data retrieval will be provided in https://github.com/SeleniumHQ/selenium/pull/16336

/** Requires Network instance to be set for particular test */
public class RequestsAssertionExtension
    implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestsAssertionExtension.class);

  private static final Map<String, CacheEntry> CACHE = new ConcurrentHashMap<>();

  @Override
  public void beforeTestExecution(@NonNull ExtensionContext context) {
    CacheEntry cacheEntry = CACHE.get(this.key(context));

    if (cacheEntry == null) {
      return;
    }

    cacheEntry
        .getNetwork()
        .onResponseStarted(
            response -> {
              ResponseLogMessage responseLogMessage =
                  new ResponseLogMessage(response, response.getRequest());

              if (responseLogMessage.status() > 399) {
                LOGGER.error(responseLogMessage.asString());
                cacheEntry.getResponseLogMessages().add(responseLogMessage);
              }
            });
  }

  @Override
  public void afterTestExecution(@NonNull ExtensionContext context) {
    String key = this.key(context);
    CacheEntry cacheEntry = CACHE.get(key);

    if (cacheEntry == null) {
      return;
    }

    cacheEntry.getNetwork().close();

    List<ResponseLogMessage> filteredResponseLogMessages =
        cacheEntry.getResponseLogMessages().stream()
            .filter(Predicate.not(cacheEntry.getIgnoringPredicate()))
            .toList();

    if (!filteredResponseLogMessages.isEmpty()) {
      Assertions.fail(
          "Failed request have been caught:" + this.toMessage(filteredResponseLogMessages));
    }

    cacheEntry.clear();
    CACHE.remove(key);
  }

  public void network(Network network, TestInfo testInfo) {
    CACHE.put(this.key(testInfo), new CacheEntry(network));
  }

  public void ignoringPredicate(Predicate<ResponseLogMessage> predicate, TestInfo testInfo) {
    CacheEntry cacheEntry = CACHE.get(this.key(testInfo));

    if (cacheEntry != null) {
      cacheEntry.setIgnoringPredicate(predicate);
    }
  }

  private String toMessage(List<ResponseLogMessage> logMessages) {
    return logMessages.stream()
        .map(ResponseLogMessage::asString)
        .collect(Collectors.joining(System.lineSeparator()));
  }

  private String key(ExtensionContext context) {
    return this.key(context.getTestClass().orElseThrow(), context.getDisplayName());
  }

  private String key(TestInfo testInfo) {
    return this.key(testInfo.getTestClass().orElseThrow(), testInfo.getDisplayName());
  }

  private String key(Class<?> testClass, String displayName) {
    return "%s.%s".formatted(testClass.getName(), displayName);
  }
}
