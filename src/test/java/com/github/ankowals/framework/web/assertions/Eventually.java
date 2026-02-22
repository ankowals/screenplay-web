package com.github.ankowals.framework.web.assertions;

import java.time.Duration;
import java.util.Arrays;
import java.util.function.Consumer;
import org.apache.commons.lang3.function.FailableCallable;
import org.apache.commons.lang3.function.FailableSupplier;
import org.apache.commons.lang3.stream.Streams;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.HamcrestCondition;
import org.awaitility.Awaitility;
import org.hamcrest.Matcher;

public class Eventually {
  private static final Duration TIMEOUT = Duration.ofSeconds(4);

  @SafeVarargs
  public static <T> void assertThat(
      FailableCallable<T, Exception> callable, Matcher<? super T>... matchers) {
    Awaitility.await()
        .atMost(TIMEOUT)
        .ignoreExceptions()
        .untilAsserted(
            () -> {
              T actual = callable.call();
              Streams.failableStream(Arrays.stream(matchers))
                  .forEach(
                      matcher ->
                          Assertions.assertThat(actual).is(HamcrestCondition.matching(matcher)));
            });
  }

  public static <T> void assertThat(
      FailableCallable<T, Exception> callable, Consumer<? super T> assertConsumer) {
    Awaitility.await()
        .atMost(TIMEOUT)
        .ignoreExceptions()
        .untilAsserted(callable::call, assertConsumer);
  }

  public static <T> void assertThat(FailableSupplier<Assert<?, T>, Throwable> assertSupplier) {
    Awaitility.await().atMost(TIMEOUT).ignoreExceptions().untilAsserted(assertSupplier::get);
  }
}
