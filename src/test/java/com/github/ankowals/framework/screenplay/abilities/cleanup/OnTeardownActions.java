package com.github.ankowals.framework.screenplay.abilities.cleanup;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.function.FailableRunnable;

public class OnTeardownActions {

  private final List<FailableRunnable<?>> runnables;

  public OnTeardownActions() {
    this.runnables = new CopyOnWriteArrayList<>();
  }

  public void add(FailableRunnable<?> runnable) {
    this.runnables.add(runnable);
  }

  public void runAll() {
    Collections.reverse(this.runnables);
    this.runnables.forEach(
        runnable -> {
          try {
            runnable.run();
          } catch (Throwable ignored) { // NOSONAR
          }
        });
    this.runnables.clear();
  }
}
