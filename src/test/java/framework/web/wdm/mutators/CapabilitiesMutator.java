package framework.web.wdm.mutators;

import java.lang.reflect.Field;
import java.net.URL;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.HttpCommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.TracedCommandExecutor;

/**
 * See AugmenterProvider implementations like DevToolsProvider or BiDiProvider to find capabilities
 * that needs to be modified
 */
public class CapabilitiesMutator {

  public void mutate(RemoteWebDriver remoteWebDriver) throws IllegalAccessException {
    URL url = this.getHubUrl(remoteWebDriver);

    String wsSessionUrl =
        String.format(
            "ws://%s:%s/session/%s",
            url.getHost(), url.getPort(), remoteWebDriver.getSessionId().toString());

    MutableCapabilities mutableCapabilities =
        (MutableCapabilities) remoteWebDriver.getCapabilities();
    mutableCapabilities.setCapability("se:cdp", wsSessionUrl + "/se/cdp");
    mutableCapabilities.setCapability("webSocketUrl", wsSessionUrl + "/se/bidi");
  }

  protected URL getHubUrl(RemoteWebDriver remoteWebDriver) throws IllegalAccessException {
    if (HttpCommandExecutor.class.isAssignableFrom(remoteWebDriver.getCommandExecutor().getClass()))
      return ((HttpCommandExecutor) remoteWebDriver.getCommandExecutor())
          .getAddressOfRemoteServer();

    TracedCommandExecutor tce = (TracedCommandExecutor) remoteWebDriver.getCommandExecutor();
    Field field = FieldUtils.getField(TracedCommandExecutor.class, "delegate", true);

    return ((HttpCommandExecutor) field.get(tce)).getAddressOfRemoteServer();
  }
}
