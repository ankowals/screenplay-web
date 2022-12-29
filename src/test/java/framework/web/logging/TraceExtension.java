package framework.web.logging;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v107.log.Log;
import org.openqa.selenium.devtools.v107.network.Network;

import java.util.List;
import java.util.Optional;

public class TraceExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private List<WebDriver> drivers;

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        drivers = new DriverProvider(context.getRequiredTestInstances()).provide();

        drivers.forEach(driver -> {
            HasDevTools devToolsDriver = (HasDevTools) driver;
            DevTools devTools = devToolsDriver.getDevTools();

            devTools.createSessionIfThereIsNotOne();
            devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            devTools.send(Log.enable());

            new ListenerRegistrar(devTools).addNetworkRequestListener()
                    .addNetworkResponseListener()
                    .addLogListener()
                    .addJavascriptExceptionListener();
        });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        drivers.forEach(driver -> {
            HasDevTools devToolsDriver = (HasDevTools) driver;
            DevTools devTools = devToolsDriver.getDevTools();
            if (devTools != null) {
                devTools.clearListeners();
                devTools.close();
            }
        });
    }
}
