package framework.screenplay.helpers;

import org.apache.commons.lang3.function.FailableRunnable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class OnShutdown {
    private static final AtomicBoolean HAS_RUN = new AtomicBoolean(false);
    private static final List<FailableRunnable<?>> RUNNABLES = new CopyOnWriteArrayList<>();

    public static void run(FailableRunnable<?> runnable) {RUNNABLES.add(runnable); }

    public static void cleanUp() {
        if (HAS_RUN.compareAndSet(false, true)) {
            Runtime.getRuntime().addShutdownHook(new Thread(OnShutdown::execute));
        }
    }

    private static void execute() {
        Collections.reverse(RUNNABLES);
        RUNNABLES.forEach(
                runnable -> {
                    try {
                        runnable.run();
                    } catch (Throwable ignored) {}
                }
        );
    }
}
