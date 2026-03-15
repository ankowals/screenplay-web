package com.github.ankowals.framework.web.wdm.bitbucket;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;

public class BitBucketChromeOptionsFactory {

  public static ChromiumOptions<?> desktop() {
    Map<String, Object> browserPreferences = BitBucketChromeOptionsFactory.getBrowserPreferences();

    // https://github.com/GoogleChrome/chrome-launcher/blob/main/docs/chrome-flags-for-tools.md
    ChromiumOptions<?> chromiumOptions =
        new ChromeOptions()
            .setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.IGNORE)
            .setExperimentalOption(
                "excludeSwitches", new String[] {"enable-automation", "disable-infobars"})
            .setExperimentalOption("prefs", browserPreferences)
            .addArguments(
                // default chromium driver flags
                // https://source.chromium.org/chromium/chromium/src/+/main:chrome/test/chromedriver/chrome_launcher.cc?q=f:chrome_launcher%20%20kDesktopSwitches&ss=chromium
                "start-maximized",
                "ignore-certificate-errors",
                "no-default-browser-check",
                "disable-field-trial-config",
                "disable-back-forward-cache",
                "disable-breakpad",
                "disable-component-update",
                "disable-component-extensions-with-background-pages",
                "disable-popup-blocking",
                "disable-notifications",
                "disable-dev-shm-usage",
                "disable-search-engine-choice-screen",
                // https://github.com/bonigarcia/webdrivermanager/issues/1477
                "disable-features=DisableLoadExtensionCommandLineSwitch,ImprovedCookieControls,LazyFrameLoading,GlobalMediaControls,DestroyProfileOnBrowserClose,MediaRouter,DialMediaRouteProvider,AcceptCHFrame,AutoExpandDetailsElement,CertificateTransparencyComponentUpdater,AvoidUnnecessaryBeforeUnloadCheckSync,Translate,InterestFeedContentSuggestions",
                "disable-background-timer-throttling",
                "disable-backgrounding-occluded-windows",
                "disable-ipc-flooding-protection",
                "disable-renderer-backgrounding",
                "propagate-iph-for-testing",
                "ash-no-nudges",
                "enable-features=NetworkService,NetworkServiceInProcess",
                "allow-pre-commit-input",
                "force-color-profile=srgb",
                "metrics-recording-only",
                "mute-audio");

    // visual assertions may require to also grab screenshots in headless
    if (Boolean.parseBoolean(System.getenv("BROWSER_IN_HEADLESS_ENABLED"))) {
      chromiumOptions.addArguments(
          "window-size=1920,1080", // defaults to 800x600
          "screen-info={1920x1080}", // https://issues.chromium.org/issues/422346607#comment4
          "headless",
          // https://chromium.googlesource.com/chromium/src/+/refs/heads/main/docs/gpu/swiftshader.md
          "use-gl=angle",
          "use-angle=swiftshader-webgl",
          "enable-unsafe-swiftshader");
    }

    return chromiumOptions;
  }

  private static Map<String, Object> getBrowserPreferences() {
    Map<String, Object> browserPreferences = new HashMap<>();
    browserPreferences.put("profile.default_content_setting_values.clipboard", 1);
    browserPreferences.put("credentials_enable_service", false);
    browserPreferences.put("profile.managed_default_content_settings.geolocation", 2);
    browserPreferences.put("autofill.profile_enabled", false);
    browserPreferences.put("profile.password_manager_enabled", false);
    browserPreferences.put("profile.password_manager_leak_detection", false);

    // assume we're running in container when bb runner detected
    // set default download dir to the build directory of a runner
    if (Boolean.parseBoolean(System.getenv("BITBUCKET_CLONE_DIR"))) {
      browserPreferences.put("download.default_directory", BitbucketDockerService.DOWNLOADS_DIR);
    }

    return browserPreferences;
  }
}
