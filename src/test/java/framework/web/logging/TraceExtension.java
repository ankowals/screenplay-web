package framework.web.logging;

import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v107.log.Log;
import org.openqa.selenium.devtools.v107.network.Network;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Works with selenium/standalone-chrome docker image but not with selenoid images
 */
public class TraceExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, InvocationInterceptor {

    private List<WebDriver> drivers;
    private boolean isSelenoid;

    public TraceExtension() {}

    public TraceExtension(boolean isSelenoid) {
        this.isSelenoid = isSelenoid;
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        drivers = new FieldDriverProvider(context).provide();
        trace(drivers);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        drivers.stream()
                .filter(withDevToolsSupport())
                .forEach(driver -> {
                    DevTools devTools;

                    try {
                        devTools = getDevTools(driver);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                    if (devTools != null)
                        devTools.close();
                });
    }

    @Override
    public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
        List<WebDriver> arguments = new ArgumentDriverProvider(invocationContext).provide();

        if (!arguments.isEmpty()) {
            drivers.addAll(arguments);
            trace(arguments);
        }

        invocation.proceed();
    }

    public TraceExtension selenoid() {
        this.isSelenoid = true;
        return this;
    }

    private void trace(List<WebDriver> drivers) {
        drivers.stream()
                .filter(withDevToolsSupport())
                .forEach(driver -> {
                    DevTools devTools;

                    try {
                        devTools = getDevTools(driver);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }

                    devTools.createSessionIfThereIsNotOne();
                    devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
                    devTools.send(Log.enable());

                    new ListenerRegistrar(devTools).addNetworkRequestListener()
                            .addNetworkResponseListener()
                            .addLogListener()
                            .addJavascriptExceptionListener();
                });
    }

    private DevTools getDevTools(WebDriver driver) throws IllegalAccessException {
        if (driver.getClass().isAssignableFrom(RemoteWebDriver.class))
            return  ((HasDevTools) new DriverAugmenter(isSelenoid).augment(driver)).getDevTools();

        return ((HasDevTools) driver).getDevTools();
    }

    //CDP not supported by every driver
    private Predicate<WebDriver> withDevToolsSupport() {
        return driver -> driver.getClass().isAssignableFrom(ChromeDriver.class) ||
                driver.getClass().isAssignableFrom(ChromiumDriver.class) ||
                driver.getClass().isAssignableFrom(EdgeDriver.class) ||
                driver.getClass().isAssignableFrom(FirefoxDriver.class);
    }

}
