package framework.web.wdm;

import framework.web.wdm.docker.BitbucketDockerService;
import io.github.bonigarcia.wdm.docker.DockerService;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import io.github.bonigarcia.wdm.webdriver.WebDriverBrowser;
import java.util.Optional;
import org.openqa.selenium.WebDriver;

public class MyChromeDriverManager extends ChromeDriverManager {

  @Override
  public MyChromeDriverManager watch() {
    this.watchEnabled = true;
    return this;
  }

  @Override
  public MyChromeDriverManager browserInDocker() {
    this.dockerEnabled = true;
    return this;
  }

  @Override
  public synchronized DockerService getDockerService() {
    if (this.dockerService == null) {
      this.dockerService = new BitbucketDockerService(this.config(), this.getResolutionCache());
    }

    return this.dockerService;
  }

  // to support running Recording via BrowserWatcher in Docker container
  @Override
  public void stopRecording(WebDriver webDriver) {
    Optional<WebDriverBrowser> maybeWebDriverBrowser = super.findWebDriverBrowser(webDriver);

    if (maybeWebDriverBrowser.isPresent()) {
      String cloneDir = System.getenv("BITBUCKET_CLONE_DIR");
      if (cloneDir != null && this.dockerEnabled) {
        new MyWebDriverBrowser(maybeWebDriverBrowser.get(), cloneDir).stopRecording();
      } else {
        maybeWebDriverBrowser.get().stopRecording();
      }
    }
  }
}
