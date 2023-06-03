package framework.web.logging;

import io.github.bonigarcia.seljup.SeleniumJupiter;

public class SelenoidSupport {

    private final SeleniumJupiter seleniumJupiter;

    public SelenoidSupport(SeleniumJupiter seleniumJupiter) {
        this.seleniumJupiter = seleniumJupiter;
    }

    public String getDevToolsPort() {
        try {
            String containerId = this.seleniumJupiter.getConfig()
                    .getManager()
                    .getDockerBrowserContainerId();

            if (containerId != null) {
                return this.seleniumJupiter.getConfig()
                        .getManager()
                        .getDockerService()
                        .getBindPort(containerId, "7070");
            }
        } catch (NullPointerException ignored) {}

        return null;
    }
}
