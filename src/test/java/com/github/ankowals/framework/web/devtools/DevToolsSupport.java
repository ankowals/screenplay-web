package com.github.ankowals.framework.web.devtools;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;

public class DevToolsSupport {

  private final DevTools devTools;

  private final NetworkDomain networkDomain;
  private final PageDomain pageDomain;
  private final EmulationDomain emulationDomain;

  public DevToolsSupport(WebDriver webDriver) {
    this.devTools = this.getDevTools(webDriver);
    this.pageDomain = new PageDomain(this.devTools);
    this.networkDomain = new NetworkDomain(this.devTools);
    this.emulationDomain = new EmulationDomain(this.devTools);
  }

  public void createSession() {
    this.devTools.createSessionIfThereIsNotOne();
  }

  public void clearListeners() {
    this.devTools.clearListeners();
  }

  public NetworkDomain getNetworkDomain() {
    return this.networkDomain;
  }

  public PageDomain getPageDomain() {
    return this.pageDomain;
  }

  public EmulationDomain getEmulationDomain() {
    return this.emulationDomain;
  }

  private DevTools getDevTools(WebDriver webDriver) {
    if (webDriver instanceof HasDevTools hasDevTools) {
      return hasDevTools.getDevTools();
    } else {
      throw new IllegalStateException("DevTools not supported by " + webDriver);
    }
  }
}
