package framework.web.wdm;

import io.github.bonigarcia.wdm.docker.DockerContainer;
import io.github.bonigarcia.wdm.docker.DockerService;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import io.github.bonigarcia.wdm.webdriver.WebDriverBrowser;

import java.util.Optional;

public class MyChromeDriverManager extends ChromeDriverManager {

    public MyChromeDriverManager browserInDocker() {
        this.dockerEnabled = true;
        return this;
    }

    @Override
    public synchronized DockerService getDockerService() {
        if (this.dockerService == null) {
            this.dockerService = new MyDockerService(this.config(),
                    this.getHttpClient(),
                    this.getResolutionCache());
        }

        return this.dockerService;
    }

    @Override
    protected synchronized void quit(WebDriverBrowser driverBrowser) {
        Optional<DockerContainer> maybeProxyContainer = Optional.empty();

        if (this.dockerService != null) {
            maybeProxyContainer = ((MyDockerService) this.dockerService).getProxyContainer(driverBrowser.getBrowserContainerId());
        }

        maybeProxyContainer.ifPresent(
                dockerContainer -> driverBrowser.getDockerContainerList().add(dockerContainer));

        super.quit(driverBrowser);
    }
}
