package framework.web.wdm;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;

public class MyWebDriverManagerFactory {

  public static MyChromeDriverManager chrome() {
    MyChromeDriverManager myChromeDriverManager = new MyChromeDriverManager();
    myChromeDriverManager.capabilities(MyWebDriverManagerFactory.chromeOptions());
    myChromeDriverManager.disableTracing();

    if (Boolean.parseBoolean(System.getenv("BROWSER_IN_DOCKER_ENABLED"))) {
      myChromeDriverManager.browserInDocker();
      myChromeDriverManager.dockerCustomImage(
          String.format("selenium/standalone-chromium:%s", System.getenv("WDM_CHROMEVERSION")));
      myChromeDriverManager.dockerScreenResolution("1920x1080x24");
    }

    return myChromeDriverManager;
  }

  private static ChromiumOptions<?> chromeOptions() {
    Map<String, Object> browserPreferences = new HashMap<>();
    browserPreferences.put("profile.default_content_setting_values.clipboard", 1);
    browserPreferences.put("credentials_enable_service", false);
    browserPreferences.put("profile.managed_default_content_settings.geolocation", 2);
    browserPreferences.put("autofill.profile_enabled", false);
    browserPreferences.put("profile.password_manager_enabled", false);
    browserPreferences.put("profile.password_manager_leak_detection", false);

    // assume we're running in container when bb runner detected
    // set default download dir to the build directory of a runner
    if (System.getenv("BITBUCKET_CLONE_DIR") != null
        && Boolean.parseBoolean(System.getenv("BROWSER_IN_DOCKER_ENABLED"))) {
      browserPreferences.put("download.default_directory", "/home/seluser/Downloads");
    }

    // https://github.com/GoogleChrome/chrome-launcher/blob/main/docs/chrome-flags-for-tools.md
    ChromiumOptions<?> chromiumOptions =
        new ChromeOptions()
            .setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT)
            .setExperimentalOption("prefs", browserPreferences)
            .setExperimentalOption("excludeSwitches", new String[] {"enable-automation"})
            .addArguments(
                "no-sandbox",
                "start-maximized",
                "ignore-gpu-blocklist",
                "ignore-certificate-errors",
                "in-process-gpu",
                "disable-popup-blocking",
                "disable-notifications",
                "disable-dev-shm-usage",
                "no-default-browser-check",
                "no-first-run",
                "disable-search-engine-choice-screen",
                "disable-storage-reset",
                "remote-debugging-port=9222",
                "disable-background-networking",
                "disable-client-side-phishing-detection",
                "disable-default-apps",
                // https://github.com/bonigarcia/webdrivermanager/issues/1477
                "disable-features=DisableLoadExtensionCommandLineSwitch,Translate,InterestFeedContentSuggestions,BackForwardCache,AcceptCHFrame,AvoidUnnecessaryBeforeUnloadCheckSync",
                "allow-pre-commit-input",
                "disable-background-networking",
                "enable-features=NetworkServiceInProcess2",
                "disable-background-timer-throttling",
                "disable-backgrounding-occluded-windows",
                "disable-client-side-phishing-detection",
                "disable-hang-monitor",
                "disable-ipc-flooding-protection",
                "disable-prompt-on-repost",
                "disable-renderer-backgrounding",
                "disable-sync",
                "force-color-profile=srgb",
                "metrics-recording-only");

    if (Boolean.parseBoolean(System.getenv("BROWSER_IN_HEADLESS_ENABLED"))) {
      chromiumOptions.addArguments(
          "headless",
          "window-size=1920,1080", // defaults to 800x600
          "force-device-scale-factor=0.7"); // WA because above setting alone does not work
    }

    chromiumOptions.enableBiDi();

    return chromiumOptions;
  }
}
