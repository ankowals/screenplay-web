package base;

import io.github.bonigarcia.wdm.docker.DockerService;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;

//poor man's WA to expose selenoid dev tools port in container
public class MyWebDriverManager extends ChromeDriverManager {

    public MyWebDriverManager browserInDocker() {
        dockerEnabled = true;
        return this;
    }

    @Override
    public synchronized DockerService getDockerService() {
        if (dockerService == null) {
            dockerService = new MyDockerService(config(), getHttpClient(), getResolutionCache());
        }
        return dockerService;
    }
}
