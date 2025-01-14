package framework.screenplay.helpers.task;

import java.util.EventListener;

@FunctionalInterface
public interface TaskListener extends EventListener {
  void taskFinished(TaskEndEvent event);
}
