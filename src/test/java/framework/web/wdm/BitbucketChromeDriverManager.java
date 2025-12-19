package framework.web.wdm;

import framework.web.wdm.docker.BitbucketDockerService;
import framework.web.wdm.mutators.CapabilitiesMutator;
import io.github.bonigarcia.wdm.docker.DockerService;
import io.github.bonigarcia.wdm.managers.ChromeDriverManager;
import io.github.bonigarcia.wdm.webdriver.WebDriverBrowser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.webextension.ExtensionPath;
import org.openqa.selenium.bidi.webextension.InstallExtensionParameters;
import org.openqa.selenium.bidi.webextension.WebExtension;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.io.Zip;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BitbucketChromeDriverManager extends ChromeDriverManager {

  private final String browserWatcherExtensionPath;

  public BitbucketChromeDriverManager() {
    this.browserWatcherExtensionPath = this.unpackBrowserWatcher().toAbsolutePath().toString();
  }

  @Override
  public synchronized DockerService getDockerService() {
    if (this.dockerService == null) {
      this.dockerService =
          new BitbucketDockerService(
              this.config(), this.getResolutionCache(), this.browserWatcherExtensionPath);
    }

    return this.dockerService;
  }

  @Override
  public synchronized WebDriver create() {
    ChromeOptions chromeOptions = new ChromeOptions().enableBiDi();

    // to install extensions via BiDi
    // enabling --remote-debugging-pipe makes the connection b/w chromedriver and the browser use a
    // pipe instead of a port,
    // disabling many CDP functionalities like devtools
    if (this.watchEnabled) {
      chromeOptions.addArguments("enable-unsafe-extension-debugging", "remote-debugging-pipe");
    }

    this.capabilities = this.capabilities.merge(chromeOptions);

    WebDriver webDriver = super.create();

    try {
      webDriver = this.augment(webDriver);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }

    if (this.watchEnabled) {
      this.installBrowserWatcherExtension(webDriver);
    }

    return webDriver;
  }

  private WebDriver augment(WebDriver webDriver) throws IllegalAccessException {
    if (webDriver.getClass().isAssignableFrom(RemoteWebDriver.class)) {
      ((RemoteWebDriver) webDriver)
          .setFileDetector(new LocalFileDetector()); // to upload files to the browser
      new CapabilitiesMutator().mutate((RemoteWebDriver) webDriver);
      WebDriver augmentedWebDriver = new Augmenter().augment(webDriver);
      this.replace(webDriver, augmentedWebDriver);

      return augmentedWebDriver;
    }

    return webDriver;
  }

  // chrome does not support installing packaged extension *.crx via BiDi
  private void installBrowserWatcherExtension(WebDriver webDriver) {
    if (this.dockerEnabled) {
      new WebExtension(webDriver)
          .install(
              new InstallExtensionParameters(
                  new ExtensionPath(BitbucketDockerService.BROWSER_WATCHER_DIR)));
    } else {
      new WebExtension(webDriver)
          .install(
              new InstallExtensionParameters(new ExtensionPath(this.browserWatcherExtensionPath)));
    }
  }

  // WDM tracks webdrivers by putting them into a list and uses object hash as a key
  // driver augmentation can modify the hash
  // let us replace it in the wdm list
  private void replace(WebDriver oldWebDriver, WebDriver newWebDriver) {
    for (WebDriverBrowser webDriverBrowser : this.webDriverList) {
      if (webDriverBrowser.getIdentityHash()
          == webDriverBrowser.calculateIdentityHash(oldWebDriver)) {
        webDriverBrowser.setDriver(newWebDriver);
      }
    }
  }

  private Path unpackBrowserWatcher() {
    File targetDir;

    try {
      Path crxPackagePath = super.getBrowserWatcherAsPath();

      // would be better to set "java.io.tmpDir" system property at runtime
      File target =
          new File(
                  BitbucketChromeDriverManager.class
                      .getProtectionDomain()
                      .getCodeSource()
                      .getLocation()
                      .getPath())
              .getParentFile();

      targetDir = Files.createTempDirectory(target.toPath(), "browserwatcher").toFile();

      try (InputStream inputStream = new FileInputStream(crxPackagePath.toFile())) {
        Zip.unzip(inputStream, targetDir);
      }

      Runtime.getRuntime().addShutdownHook(new Thread(() -> FileHandler.delete(targetDir)));

    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return targetDir.toPath();
  }
}
