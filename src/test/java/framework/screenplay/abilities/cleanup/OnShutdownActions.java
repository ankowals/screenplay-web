package framework.screenplay.abilities.cleanup;

import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang3.function.FailableRunnable;

public class OnShutdownActions {

  private static final AtomicBoolean HAS_RUN = new AtomicBoolean(false);
  private static final OnTeardownActions ON_TEARDOWN_ACTIONS = new OnTeardownActions();

  public static void add(FailableRunnable<?> runnable) {
    ON_TEARDOWN_ACTIONS.add(runnable);
  }

  public static void runAllAtShutdown() {
    if (HAS_RUN.compareAndSet(false, true)) {
      Runtime.getRuntime().addShutdownHook(new Thread(ON_TEARDOWN_ACTIONS::runAll));
    }
  }
}
