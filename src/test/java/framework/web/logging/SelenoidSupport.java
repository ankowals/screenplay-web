package framework.web.logging;

import io.github.bonigarcia.seljup.SeleniumJupiter;

public class SelenoidSupport {

    public static String getDevToolsPort(SeleniumJupiter seleniumJupiter) {
        try {
            String containerId = seleniumJupiter.getConfig().getManager().getDockerBrowserContainerId();
            if (containerId != null) {
                return seleniumJupiter.getConfig().getManager()
                        .getDockerService()
                        .getBindPort(containerId, "7070");
            }
        } catch (NullPointerException ignored) {}

        return null;
    }
}
