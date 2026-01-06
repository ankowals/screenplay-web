package framework.screenplay.helpers;

import framework.screenplay.Interaction;
import framework.screenplay.actor.Actor;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import org.apache.commons.lang3.function.FailableCallable;
import org.apache.commons.lang3.function.FailableRunnable;

public class InParallel {

  private final Actor[] actors;

  private InParallel(Actor[] actors) {
    this.actors = actors;
  }

  public static InParallel actors(Actor... actors) {
    return new InParallel(actors);
  }

  /**
   * InParallel.perform(() -> actor1.attemptsTo(Buy.product("Dress")), () ->
   * actor2.attemptsTo(Buy.product("Other Dress")));
   */
  public static void perform(FailableRunnable<?>... failableRunnables) {
    InParallel.perform(8, failableRunnables);
  }

  public static void perform(int parallelism, FailableRunnable<?>... failableRunnables) {
    try (ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism)) {
      CompletableFuture<?>[] completableFutures =
          Arrays.stream(failableRunnables)
              .map(failableRunnable -> InParallel.toFuture(failableRunnable, forkJoinPool))
              .toList()
              .toArray(new CompletableFuture[0]);

      CompletableFuture.allOf(completableFutures).join();
    }
  }

  @SafeVarargs
  public static <T> List<T> performAndGet(FailableCallable<T, Exception>... callables) {
    return InParallel.performAndGet(8, callables);
  }

  @SafeVarargs
  public static <T> List<T> performAndGet(
      int parallelism, FailableCallable<T, Exception>... callables) {
    try (ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism)) {
      List<CompletableFuture<T>> completableFutures =
          Arrays.stream(callables)
              .map(callable -> InParallel.toFuture(callable, forkJoinPool))
              .toList();

      CompletableFuture<List<T>> allOfFuture =
          CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
              .thenApply(t -> completableFutures.stream().map(CompletableFuture::join).toList());

      return allOfFuture.join();
    }
  }

  /** InParallel.actors(actor1, actor2).eachAttemptsTo(() -> Buy.product("Dress")); */
  @SafeVarargs
  public final void eachAttemptsTo(Supplier<Interaction>... suppliers) {
    this.eachAttemptsTo(8, suppliers);
  }

  @SafeVarargs
  public final void eachAttemptsTo(int parallelism, Supplier<Interaction>... suppliers) {
    List<FailableRunnable<Exception>> failableRunnables =
        Arrays.stream(suppliers)
            .flatMap(
                supplier ->
                    Arrays.stream(this.actors)
                        .map(
                            actor ->
                                (FailableRunnable<Exception>)
                                    () -> actor.attemptsTo(supplier.get())))
            .toList();

    InParallel.perform(parallelism, failableRunnables.toArray(new FailableRunnable[] {}));
  }

  private static CompletableFuture<Void> toFuture(
      FailableRunnable<?> failableRunnable, ForkJoinPool forkJoinPool) {
    return CompletableFuture.runAsync(
        () -> {
          try {
            failableRunnable.run();
          } catch (Throwable e) {
            throw new RuntimeException(e);
          }
        },
        forkJoinPool);
  }

  private static <T> CompletableFuture<T> toFuture(
      FailableCallable<T, Exception> failableCallable, ForkJoinPool forkJoinPool) {
    return CompletableFuture.supplyAsync(
        () -> {
          try {
            return failableCallable.call();
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        },
        forkJoinPool);
  }
}
