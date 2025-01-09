package framework.screenplay.helpers;

import framework.screenplay.Interaction;
import framework.screenplay.actor.Actor;
import org.testcontainers.shaded.org.apache.commons.lang3.function.FailableConsumer;
import org.testcontainers.shaded.org.apache.commons.lang3.function.FailableRunnable;

// ToDo: add entry to report for each task
/**
 * Task.where("Admin provisions new device", () -> { Device actualDevice = DeviceCreator.aNewOne();
 * Assign.device(actualDevice).to(customer).performedBy(actor);
 * Provision.device(actualDevice).performedBy(actor);
 * Activate.device(actualDevice).performedBy(actor); })
 */
public class Task {

  public static void where(String description, FailableRunnable<Exception> runnable)
      throws Exception {
    System.out.println(description);
    runnable.run();
  }

  public static Interaction where(
      String description, FailableConsumer<Actor, Exception> customizer) {
    return actor -> {
      System.out.println(description);
      customizer.accept(actor);
    };
  }
}
