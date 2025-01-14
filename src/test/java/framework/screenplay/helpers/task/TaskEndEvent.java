package framework.screenplay.helpers.task;

import java.util.EventObject;

public class TaskEndEvent extends EventObject {

  private final String taskName;
  private final TestIdentifier testIdentifier;

  public TaskEndEvent(Object source, String taskName, TestIdentifier testIdentifier) {
    super(source);
    this.taskName = taskName;
    this.testIdentifier = testIdentifier;
  }

  public String getTaskName() {
    return this.taskName;
  }

  public TestIdentifier getTestIdentifier() {
    return this.testIdentifier;
  }
}
