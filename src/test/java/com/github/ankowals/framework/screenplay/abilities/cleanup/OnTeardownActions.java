package com.github.ankowals.framework.screenplay.abilities.cleanup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.function.FailableRunnable;

public class OnTeardownActions {

  private final List<FailableRunnable<?>> runnables;
  private final List<Throwable> errors;

  public OnTeardownActions() {
    this.runnables = new CopyOnWriteArrayList<>();
    this.errors = new ArrayList<>();
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
          } catch (Throwable e) {
            this.errors.add(e);
          }
        });
    this.runnables.clear();

    if (!this.errors.isEmpty()) {
      throw new CleanUpFailedException(this.errors);
    }
  }
}
