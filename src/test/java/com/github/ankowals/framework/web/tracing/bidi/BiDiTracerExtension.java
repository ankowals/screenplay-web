package com.github.ankowals.framework.web.tracing.bidi;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.bidi.module.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// support for data retrieval will be provided in https://github.com/SeleniumHQ/selenium/pull/16336
public class BiDiTracerExtension
    implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

  private static final Logger LOGGER = LoggerFactory.getLogger(BiDiTracerExtension.class);

  private static final Map<String, CacheEntry> CACHE = new ConcurrentHashMap<>();

  @Override
  public void beforeTestExecution(@NonNull ExtensionContext context) {
    CacheEntry cacheEntry = CACHE.get(this.key(context));

    if (cacheEntry == null) {
      return;
    }

    cacheEntry
        .getNetwork()
        .onBeforeRequestSent(request -> LOGGER.info(new RequestLogMessage(request).asString()));
    cacheEntry
        .getNetwork()
        .onResponseStarted(response -> LOGGER.info(new ResponseLogMessage(response).asString()));
  }

  @Override
  public void afterTestExecution(@NonNull ExtensionContext context) {
    String key = this.key(context);
    CacheEntry cacheEntry = CACHE.get(key);

    if (cacheEntry == null) {
      return;
    }

    cacheEntry.getNetwork().close();
    CACHE.remove(key);
  }

  public void network(Network network, TestInfo testInfo) {
    CACHE.put(this.key(testInfo), new CacheEntry(network));
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
