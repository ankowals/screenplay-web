package com.github.ankowals.framework.web.wdm.session.storage;

import org.openqa.selenium.WebDriver;

public class SessionStorage extends WebStorage {
  public SessionStorage(WebDriver webDriver) {
    super(webDriver, "sessionStorage");
  }
}
