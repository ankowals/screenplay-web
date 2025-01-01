package framework.screenplay.hooks;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang3.function.FailableRunnable;

public class OnShutdown {
  private static final AtomicBoolean HAS_RUN = new AtomicBoolean(false);
  private static final List<FailableRunnable<?>> RUNNABLES = new CopyOnWriteArrayList<>();

  public static void defer(FailableRunnable<?> runnable) {
    RUNNABLES.add(runnable);
  }

  public static void run() {
    if (HAS_RUN.compareAndSet(false, true)) {
      Runtime.getRuntime().addShutdownHook(new Thread(OnShutdown::doRun));
    }
  }

  private static void doRun() {
    Collections.reverse(RUNNABLES);
    RUNNABLES.forEach(
        runnable -> {
          try {
            runnable.run();
          } catch (Throwable ignored) {
          }
        });
  }
}
