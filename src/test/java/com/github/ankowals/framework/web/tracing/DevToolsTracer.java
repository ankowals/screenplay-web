package com.github.ankowals.framework.web.tracing;

import java.util.Optional;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v144.network.Network;

// ToDo: add support for screencasting using devTools??
public class DevToolsTracer {

  private final DevTools devTools;

  public DevToolsTracer(DevTools devTools) {
    this.devTools = devTools;
  }

  public void trace() {
    this.devTools.createSessionIfThereIsNotOne();
    this.devTools.send(
        Network.enable(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()));
    /*
    this.devTools.send(
        Page.startScreencast(
            Optional.of(Page.StartScreencastFormat.PNG), //format
            Optional.of(100), //quality
            Optional.of(1920), //maxWidth
            Optional.of(1080), //maxHeight
            Optional.of(1))); //everyNthFrame

    this.devTools.send(Log.enable());
     */

    new ListenerRegistrar(this.devTools)
        // .addPageScreencastListener()
        .addNetworkRequestListener()
        .addNetworkResponseListener();
    // .addConsoleLogListener()
    // .addJavascriptExceptionListener();
  }
}
