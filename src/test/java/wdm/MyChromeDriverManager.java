package wdm;

import io.github.bonigarcia.wdm.docker.DockerService;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;

public class MyChromeDriverManager extends ChromeDriverManager {

    public MyChromeDriverManager browserInDocker() {
        this.dockerEnabled = true;
        return this;
    }

    @Override
    public synchronized DockerService getDockerService() {
        if (this.dockerService == null) {
            this.dockerService = new MyDockerService(config(), getHttpClient(), getResolutionCache());
        }
        return this.dockerService;
    }
}
