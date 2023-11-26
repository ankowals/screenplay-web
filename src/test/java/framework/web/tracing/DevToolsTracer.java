package framework.web.tracing;

import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v119.log.Log;
import org.openqa.selenium.devtools.v119.network.Network;

import java.util.Optional;

public class DevToolsTracer {

    private final DevTools devTools;

    public DevToolsTracer(DevTools devTools) {
        this.devTools = devTools;
    }

    public void trace() {
        this.devTools.createSessionIfThereIsNotOne();
        this.devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        this.devTools.send(Log.enable());

        new ListenerRegistrar(this.devTools).addNetworkRequestListener()
                .addNetworkResponseListener()
                .addConsoleLogListener()
                .addJavascriptExceptionListener();
    }
}
