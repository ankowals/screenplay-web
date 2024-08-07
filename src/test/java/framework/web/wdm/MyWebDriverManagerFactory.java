package framework.web.wdm;

import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

public class MyWebDriverManagerFactory {

    public static MyChromeDriverManager chrome() {
        MyChromeDriverManager myChromeDriverManager = new MyChromeDriverManager();
        myChromeDriverManager.capabilities(MyWebDriverManagerFactory.chromeOptions());
        myChromeDriverManager.enableRecording();

        if (System.getenv("BROWSER_VERSION") != null)
            myChromeDriverManager.browserVersion(System.getenv("BROWSER_VERSION"));

        if (System.getenv("ENABLE_BROWSER_IN_DOCKER_CONTAINER") != null)
            myChromeDriverManager.browserInDocker();

        return myChromeDriverManager;
    }

    private static ChromeOptions chromeOptions() {
        return new ChromeOptions()
                .setExperimentalOption("prefs",
                        Map.of(
                                "profile.managed_default_content_settings.geolocation",
                                2 //switch off location services
                        ))
                .setExperimentalOption("excludeSwitches", new String[]{"enable-automation"})
                .addArguments("disable-infobars",
                        "--no-sandbox",
                        "--disable-gpu",
                        "--start-maximized",
                        "--ignore-certificate-errors",
                        "--disable-popup-blocking",
                        "--disable-extensions",
                        "--disable-dev-shm-usage",
                        "--incognito",
                        "--no-default-browser-check",
                        "--disable-search-engine-choice-screen",
                        "--window-size=1920,1080");
    }
}
