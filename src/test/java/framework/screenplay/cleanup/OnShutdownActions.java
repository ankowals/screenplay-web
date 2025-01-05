package framework.screenplay.cleanup;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang3.function.FailableRunnable;

public class OnShutdownActions {
  private static final AtomicBoolean HAS_RUN = new AtomicBoolean(false);
  private static final List<FailableRunnable<?>> RUNNABLES = new CopyOnWriteArrayList<>();

  public static void add(FailableRunnable<?> runnable) {
    RUNNABLES.add(runnable);
  }

  public static void runAll() {
    if (HAS_RUN.compareAndSet(false, true)) {
      Runtime.getRuntime().addShutdownHook(new Thread(OnShutdownActions::doRunAll));
    }
  }

  private static void doRunAll() {
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
