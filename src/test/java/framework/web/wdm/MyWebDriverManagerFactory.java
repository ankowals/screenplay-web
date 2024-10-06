package framework.web.wdm;

import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;

import java.util.Map;

public class MyWebDriverManagerFactory {

    public static MyChromeDriverManager chrome() {
        MyChromeDriverManager myChromeDriverManager = new MyChromeDriverManager();
        myChromeDriverManager.capabilities(MyWebDriverManagerFactory.chromeOptions());
        myChromeDriverManager.disableTracing();

        if (System.getenv("ENABLE_BROWSER_IN_DOCKER_CONTAINER") != null) {
            myChromeDriverManager.browserInDocker();
            myChromeDriverManager.dockerScreenResolution("1920x1080x24");
        }

        return myChromeDriverManager;
    }

    private static ChromiumOptions<?> chromeOptions() {
        ChromiumOptions<?> chromiumOptions = new ChromeOptions()
                .setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT)
                .setExperimentalOption("prefs",
                        Map.of(
                                "profile.default_content_setting_values.clipboard",
                                1,
                                "credentials_enable_service",
                                false,
                                "profile.managed_default_content_settings.geolocation",
                                2, //switch off location services
                                "autofill.profile_enabled",
                                false //disable autofill banner
                        ))
                .setExperimentalOption("excludeSwitches", new String[]{"enable-automation"})
                .addArguments("disable-infobars",
                        "--no-sandbox",
                        "--disable-gpu",
                        "--start-maximized",
                        "--ignore-certificate-errors",
                        "--disable-popup-blocking",
                        "--disable-extensions",
                        "--disable-notifications",
                        "--disable-application-cache",
                        "--disable-dev-shm-usage",
                        //"--incognito",
                        "--no-default-browser-check",
                        "--disable-search-engine-choice-screen");

        chromiumOptions.setCapability("webSocketUrl", true);

        return chromiumOptions;
    }
}
