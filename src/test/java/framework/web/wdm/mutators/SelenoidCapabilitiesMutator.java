package framework.web.wdm.mutators;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

/** Selenoid browser container exposes devTools at port 7070 */
public class SelenoidCapabilitiesMutator extends CapabilitiesMutator {

  private final WebDriverManager webDriverManager;

  public SelenoidCapabilitiesMutator(WebDriverManager webDriverManager) {
    this.webDriverManager = webDriverManager;
  }

  @Override
  public void mutate(RemoteWebDriver remoteWebDriver) throws IllegalAccessException {
    MutableCapabilities mutableCapabilities =
        (MutableCapabilities) remoteWebDriver.getCapabilities();

    URL url = this.getHubUrl(remoteWebDriver);

    String containerId = this.webDriverManager.getDockerBrowserContainerId(remoteWebDriver);

    if (StringUtils.isEmpty(containerId)) {
      return;
    }

    String devToolsPort = this.getExposedPort(containerId, 7070);

    if (StringUtils.isEmpty(devToolsPort)) {
      return;
    }

    String cdpUrl =
        String.format(
            "http://%s:%s/devtools/%s/",
            url.getHost(), devToolsPort, remoteWebDriver.getSessionId().toString());

    mutableCapabilities.setCapability("se:cdpVersion", mutableCapabilities.getBrowserVersion());
    mutableCapabilities.setCapability("se:cdp", cdpUrl);
  }

  private String getExposedPort(String containerId, int port) {
    try {
      return this.webDriverManager
          .getDockerService()
          .getBindPort(containerId, String.valueOf(port));
    } catch (NullPointerException ignored) {
    }

    return "";
  }
}
