package framework.web.wdm;

import java.util.Map;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;

public class MyWebDriverManagerFactory {

  public static MyChromeDriverManager chrome() {
    MyChromeDriverManager myChromeDriverManager = new MyChromeDriverManager();
    myChromeDriverManager.capabilities(MyWebDriverManagerFactory.chromeOptions());
    myChromeDriverManager.disableTracing();

    if (Boolean.parseBoolean(System.getenv("ENABLE_BROWSER_IN_DOCKER_CONTAINER"))) {
      myChromeDriverManager.browserInDocker();
      myChromeDriverManager.dockerCustomImage(
          String.format("selenium/standalone-chromium:%s", System.getenv("WDM_CHROMEVERSION")));
      myChromeDriverManager.dockerScreenResolution("1920x1080x24");
    }

    return myChromeDriverManager;
  }

  private static ChromiumOptions<?> chromeOptions() {
    ChromiumOptions<?> chromiumOptions =
        new ChromeOptions()
            .setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT)
            .setExperimentalOption(
                "prefs",
                Map.of(
                    "profile.default_content_setting_values.clipboard",
                    1,
                    "credentials_enable_service",
                    false,
                    "profile.managed_default_content_settings.geolocation",
                    2, // switch off location services
                    "autofill.profile_enabled",
                    false, // disable autofill banner,
                    "profile.password_manager_enabled",
                    false,
                    "profile.password_manager_leak_detection",
                    false))
            .setExperimentalOption("excludeSwitches", new String[] {"enable-automation"})
            .addArguments(
                "disable-infobars",
                "--no-sandbox",
                "--start-maximized",
                "--ignore-certificate-errors",
                "--disable-popup-blocking",
                "--disable-extensions",
                "--disable-notifications",
                "--disable-dev-shm-usage",
                // "--incognito",
                "--no-default-browser-check",
                "--disable-search-engine-choice-screen",
                "password-store=basic");

    if (Boolean.parseBoolean(System.getenv("ENABLE_BROWSER_IN_HEADLESS_MODE"))) {
      chromiumOptions.addArguments("--headless=new");
    }

    chromiumOptions.enableBiDi();

    return chromiumOptions;
  }
}
