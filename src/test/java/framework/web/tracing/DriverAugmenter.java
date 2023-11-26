package framework.web.tracing;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.*;

import java.lang.reflect.Field;
import java.net.URL;

public class DriverAugmenter {

    private final WebDriverManager webDriverManager;

    public DriverAugmenter(WebDriverManager webDriverManager) {
        this.webDriverManager = webDriverManager;
    }

    public WebDriver augment(WebDriver driver) throws IllegalAccessException {
        if (!driver.getClass().isAssignableFrom(RemoteWebDriver.class))
            return driver;

        RemoteWebDriver remoteWebDriver = (RemoteWebDriver) driver;

        URL hubUrl = this.getHubUrl(remoteWebDriver);

        MutableCapabilities mutableCapabilities = (MutableCapabilities) remoteWebDriver.getCapabilities();

        String cdpUrl = String.format("ws://%s:%s/session/%s/se/cdp", hubUrl.getHost(), hubUrl.getPort(), this.getSessionId(remoteWebDriver));
        mutableCapabilities.setCapability("se:cdp", cdpUrl);

        //selenoid for devTools uses port 7070, we need to forward 7070 in container and pass it here
        String selenoidDevToolsPort = this.getContainerExposedPort(7070);
        if (!this.isNullOrEmpty(selenoidDevToolsPort)) {
            cdpUrl = String.format("http://%s:%s/devtools/%s/", hubUrl.getHost(), selenoidDevToolsPort, this.getSessionId(remoteWebDriver));
            mutableCapabilities.setCapability("se:cdpVersion", mutableCapabilities.getBrowserVersion());
            mutableCapabilities.setCapability("se:cdp", cdpUrl);
        }

        return new Augmenter().augment(driver);
    }

    private String getSessionId(RemoteWebDriver remoteWebDriver) {
        return remoteWebDriver.getSessionId().toString();
    }

    private URL getHubUrl(RemoteWebDriver remoteWebDriver) throws IllegalAccessException {
        if (HttpCommandExecutor.class.isAssignableFrom(remoteWebDriver.getCommandExecutor().getClass()))
            return ((HttpCommandExecutor) remoteWebDriver.getCommandExecutor()).getAddressOfRemoteServer();

        TracedCommandExecutor tce = (TracedCommandExecutor) remoteWebDriver.getCommandExecutor();
        Field field = FieldUtils.getField(TracedCommandExecutor.class, "delegate", true);

        return ((HttpCommandExecutor) field.get(tce)).getAddressOfRemoteServer();
    }

    private boolean isNullOrEmpty(String text) {
        return text == null || text.isEmpty();
    }

    private String getContainerExposedPort(int port) {
        try {
            String containerId = this.webDriverManager.getDockerBrowserContainerId();
            if (!this.isNullOrEmpty(containerId))
                return this.webDriverManager.getDockerService().getBindPort(containerId, String.valueOf(port));

        } catch(NullPointerException ignored) {}

        return "";
    }
}
