package screenplay.task;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Log;
import com.aventstack.extentreports.model.Test;
import framework.reporting.ExtentReportExtension;
import framework.screenplay.helpers.task.TaskEndEvent;
import framework.screenplay.helpers.task.TaskListener;
import framework.screenplay.helpers.task.TestIdentifier;
import java.io.File;

public class TaskReportingReportExtension extends ExtentReportExtension implements TaskListener {

  public TaskReportingReportExtension(File file) {
    super(file);
  }

  @Override
  public void taskFinished(TaskEndEvent event) {
    Log log = Log.builder().status(Status.INFO).details(event.getTaskName()).build();
    this.findTest(event.getTestIdentifier()).addLog(log);
  }

  private Test findTest(TestIdentifier testIdentifier) {
    return this.extentReport
        .getReport()
        .findTest(String.format("%s.%s", testIdentifier.className(), testIdentifier.methodName()))
        .orElseThrow();
  }
}
