package com.github.ankowals.framework.web.extensions;

import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;

public class CacheKey {
  public static String from(ExtensionContext extensionContext) {
    return CacheKey.from(
        extensionContext.getTestClass().orElseThrow(), extensionContext.getDisplayName());
  }

  public static String from(TestInfo testInfo) {
    return CacheKey.from(testInfo.getTestClass().orElseThrow(), testInfo.getDisplayName());
  }

  private static String from(Class<?> testClass, String displayName) {
    return "%s.%s".formatted(testClass.getName(), displayName);
  }
}
