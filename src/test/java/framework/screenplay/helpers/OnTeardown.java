package framework.screenplay.helpers;

import org.apache.commons.lang3.function.FailableRunnable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OnTeardown {

    private final List<FailableRunnable<?>> runnables;

    public OnTeardown() {
        this.runnables = new CopyOnWriteArrayList<>();
    }

    public void run(FailableRunnable<?> runnable) { this.runnables.add(runnable); }

    public void cleanUp() {
        Collections.reverse(this.runnables);
        this.runnables.forEach(
                runnable -> {
                    try {
                        runnable.run();
                    } catch (Throwable ignored) {}
                }
        );
    }
}
