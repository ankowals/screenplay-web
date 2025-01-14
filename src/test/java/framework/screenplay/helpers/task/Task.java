package framework.screenplay.helpers.task;

import org.apache.commons.lang3.function.FailableRunnable;

/**
 * task.where(testInfo, "Admin provisions new device", () -> { Device actualDevice =
 * DeviceCreator.aNewOne(); Provision.device(actualDevice).performedBy(actor); })
 */
public class Task {

  public Task(TaskListener taskListener) {
    this.taskListener = taskListener;
  }

  private final TaskListener taskListener;

  public void where(
      TestIdentifier testIdentifier, String description, FailableRunnable<Exception> runnable)
      throws Exception {
    try {
      runnable.run();
    } finally {
      this.taskListener.taskFinished(new TaskEndEvent(this, description, testIdentifier));
    }
  }
}
