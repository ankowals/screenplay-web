package framework.web.wdm;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.nio.file.Paths;
import org.openqa.selenium.chromium.ChromiumOptions;

public class MyWebDriverManagerFactory {

  public static WebDriverManager chrome(ChromiumOptions<?> chromiumOptions) {
    WebDriverManager wdm =
        new BitbucketChromeDriverManager(
            Paths.get("src", "test", "resources", "browserwatcher-2.1.0"));

    if (Boolean.parseBoolean(System.getenv("BROWSER_WATCHER_ENABLED"))) {
      wdm.watch(); // install browser watcher extension the classic way
    }

    if (Boolean.parseBoolean(System.getenv("BROWSER_IN_DOCKER_ENABLED"))) {
      wdm.browserInDocker();
      wdm.dockerCustomImage(
          String.format(
              "selenium/standalone-chromium:%s",
              System.getenv("WDM_CHROMEVERSION"))); // chromium images works on arm and x86
      wdm.dockerScreenResolution("1920x1080x24");
    }

    wdm.capabilities(chromiumOptions);
    wdm.disableTracing();

    return wdm;
  }
}
