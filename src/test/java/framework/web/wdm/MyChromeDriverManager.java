package framework.web.wdm;

import framework.web.wdm.docker.BitbucketDockerService;
import io.github.bonigarcia.wdm.docker.DockerService;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;

public class MyChromeDriverManager extends ChromeDriverManager {

  @Override
  public MyChromeDriverManager browserInDocker() {
    this.dockerEnabled = true;
    return this;
  }

  @Override
  public synchronized DockerService getDockerService() {
    if (this.dockerService == null) {
      this.dockerService =
          new BitbucketDockerService(
              this.config(), this.getHttpClient(), this.getResolutionCache());
    }

    return this.dockerService;
  }
}
