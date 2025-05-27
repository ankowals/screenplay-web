package framework.web.tracing;

import java.util.Optional;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v134.log.Log;
import org.openqa.selenium.devtools.v134.network.Network;
import org.openqa.selenium.devtools.v134.page.Page;

public class DevToolsTracer {

  private final DevTools devTools;

  public DevToolsTracer(DevTools devTools) {
    this.devTools = devTools;
  }

  public void trace() {
    this.devTools.createSessionIfThereIsNotOne();
    this.devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
    /*
     client.send('Page.startScreencast', {
    format: 'png',
    quality: 100,
    maxWidth: 1920,
    maxHeight: 1080,
    everyNthFrame: 1,
     });
      */
    this.devTools.send(
        Page.startScreencast(
            Optional.of(Page.StartScreencastFormat.PNG),
            Optional.of(100),
            Optional.of(1920),
            Optional.of(1080),
            Optional.of(1)));
    this.devTools.send(Log.enable());

    new ListenerRegistrar(this.devTools)
        // .addPageScreencastListener()
        .addNetworkRequestListener()
        .addNetworkResponseListener()
        .addConsoleLogListener()
        .addJavascriptExceptionListener();
  }
}
