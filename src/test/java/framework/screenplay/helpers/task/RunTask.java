package framework.screenplay.helpers.task;

import framework.screenplay.Interaction;
import framework.screenplay.actor.Actor;
import org.testcontainers.shaded.org.apache.commons.lang3.function.FailableConsumer;

public class RunTask {
  public static Interaction where(
      String description, FailableConsumer<Actor, Exception> customizer) {
    return actor -> {
      System.out.println(description);
      customizer.accept(actor);
    };
  }
}
