package framework.screenplay.hooks;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.lang3.function.FailableRunnable;

public class OnTeardown {

  private final List<FailableRunnable<?>> runnables;

  public OnTeardown() {
    this.runnables = new CopyOnWriteArrayList<>();
  }

  public void defer(FailableRunnable<?> runnable) {
    this.runnables.add(runnable);
  }

  public void run() {
    Collections.reverse(this.runnables);
    this.runnables.forEach(
        runnable -> {
          try {
            runnable.run();
          } catch (Throwable ignored) {
          }
        });
  }
}
