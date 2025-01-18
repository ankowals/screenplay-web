package framework.screenplay.helpers;

import java.util.Arrays;
import java.util.concurrent.*;
import org.apache.commons.lang3.function.FailableRunnable;
import org.apache.commons.lang3.stream.Streams;

public class InParallel {

  /**
   * InParallel.perform( () -> actor1.attemptsTo(Find.productDetails("Dress")), () ->
   * actor2.attemptsTo(Find.productDetails("Other Dress")));
   */
  public static void perform(FailableRunnable<?>... failableRunnables) {
    InParallel.perform(8, failableRunnables);
  }

  public static void perform(int parallelism, FailableRunnable<?>... failableRunnables) {
    try (ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism)) {
      forkJoinPool
          .submit(
              () ->
                  Streams.failableStream(Arrays.stream(failableRunnables).parallel())
                      .forEach(FailableRunnable::run))
          .join();
    }
  }
}
