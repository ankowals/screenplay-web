package framework.web.tracing;

import java.util.Optional;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v131.log.Log;
import org.openqa.selenium.devtools.v131.network.Network;

public class DevToolsTracer {

  private final DevTools devTools;

  public DevToolsTracer(DevTools devTools) {
    this.devTools = devTools;
  }

  public void trace() {
    this.devTools.createSessionIfThereIsNotOne();
    this.devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
    this.devTools.send(Log.enable());

    new ListenerRegistrar(this.devTools)
        .addNetworkRequestListener()
        .addNetworkResponseListener()
        .addConsoleLogListener()
        .addJavascriptExceptionListener();
  }
}
