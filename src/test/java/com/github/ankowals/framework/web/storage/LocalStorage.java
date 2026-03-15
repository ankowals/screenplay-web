package com.github.ankowals.framework.web.storage;

import org.openqa.selenium.WebDriver;

public class LocalStorage extends WebStorage {
  public LocalStorage(WebDriver webDriver) {
    super(webDriver, "localStorage");
  }
}
