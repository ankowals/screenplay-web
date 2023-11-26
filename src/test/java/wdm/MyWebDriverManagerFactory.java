package wdm;

import org.openqa.selenium.chrome.ChromeOptions;

public class MyWebDriverManagerFactory {

    public static MyChromeDriverManager chrome() {
        MyChromeDriverManager myChromeDriverManager = new MyChromeDriverManager();
        myChromeDriverManager.capabilities(MyWebDriverManagerFactory.chromeOptions());
        myChromeDriverManager.browserVersion("119");
        myChromeDriverManager.enableRecording();

       // if (System.getenv("ENABLE_BROWSER_IN_DOCKER_CONTAINER") != null)
            myChromeDriverManager.browserInDocker();

        return myChromeDriverManager;
    }

    private static ChromeOptions chromeOptions() {
        return new ChromeOptions()
                .setExperimentalOption("excludeSwitches", new String[]{"enable-automation"})
                .addArguments("disable-infobars",
                        "--no-sandbox",
                        "--disable-gpu",
                        "--start-maximized",
                        "--ignore-certificate-errors",
                        "--disable-popup-blocking",
                        "--disable-extensions",
                        "--disable-dev-shm-usage",
                        "--incognito");
    }
}
