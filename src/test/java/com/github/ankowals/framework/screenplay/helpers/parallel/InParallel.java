package com.github.ankowals.framework.screenplay.helpers.parallel;

import com.github.ankowals.framework.screenplay.Interaction;
import com.github.ankowals.framework.screenplay.Question;
import com.github.ankowals.framework.screenplay.actor.Actor;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;
import org.apache.commons.lang3.function.FailableCallable;
import org.apache.commons.lang3.function.FailableRunnable;

public class InParallel implements OneAttemptsTo, EachAttemptsTo {

  private final Actor[] actors;

  private InParallel(Actor[] actors) {
    this.actors = actors;
  }

  public static OneAttemptsTo actor(Actor actor) {
    return new InParallel(new Actor[] {actor});
  }

  public static EachAttemptsTo actors(Actor... actors) {
    return new InParallel(actors);
  }

  /** InParallel.actor(actor1).attemptsTo(Buy.product("Dress"), Buy.product("Other Dress")); */
  @Override
  public void attemptsTo(Interaction... interactions) {
    this.attemptsTo(8, interactions);
  }

  @Override
  public void attemptsTo(int parallelism, Interaction... interactions) {
    this.actors[0].attemptsTo(
        actor -> {
          FailableRunnable<?>[] failableRunnables =
              Arrays.stream(interactions)
                  .map(
                      interaction ->
                          (FailableRunnable<Exception>) () -> interaction.performAs(actor))
                  .toArray(FailableRunnable[]::new);

          InParallel.doRun(parallelism, failableRunnables);
        });
  }

  /** InParallel.actor(actor1).asksFor(TheUser.details("Bob"), TheUser.details("Jenny")); */
  @Override
  @SafeVarargs
  public final <T> List<T> asksFor(Question<T>... questions) {
    return this.asksFor(8, questions);
  }

  @Override
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public final <T> List<T> asksFor(int parallelism, Question<T>... questions) {
    Actor actor = this.actors[0];
    FailableCallable<T, Exception>[] failableCallables =
        Arrays.stream(questions)
            .map(question -> (FailableCallable<T, Exception>) () -> actor.asksFor(question))
            .toArray(FailableCallable[]::new);

    return InParallel.doCall(parallelism, failableCallables);
  }

  /** InParallel.actors(actor1, actor2).eachAttemptsTo(() -> Buy.product("Dress")); */
  @Override
  @SafeVarargs
  public final void eachAttemptsTo(Supplier<Interaction>... suppliers) {
    this.eachAttemptsTo(8, suppliers);
  }

  @Override
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

    InParallel.doRun(parallelism, failableRunnables.toArray(new FailableRunnable[] {}));
  }

  /** InParallel.actors(actor1, actor2).eachAsksFor(() -> TheUser.details("Bob")) */
  @Override
  @SafeVarargs
  public final <T> List<T> eachAsksFor(Supplier<Question<T>>... suppliers) {
    return this.eachAsksFor(8, suppliers);
  }

  @Override
  @SafeVarargs
  @SuppressWarnings("unchecked")
  public final <T> List<T> eachAsksFor(int parallelism, Supplier<Question<T>>... suppliers) {
    FailableCallable<T, Exception>[] failableCallables =
        Arrays.stream(suppliers)
            .flatMap(
                supplier ->
                    Arrays.stream(this.actors)
                        .map(
                            actor ->
                                (FailableCallable<T, Exception>)
                                    () -> actor.asksFor(supplier.get())))
            .toArray(FailableCallable[]::new);

    return InParallel.doCall(parallelism, failableCallables);
  }

  private static void doRun(int parallelism, FailableRunnable<?>... failableRunnables) {
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
  private static <T> List<T> doCall(int parallelism, FailableCallable<T, Exception>... callables) {
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
