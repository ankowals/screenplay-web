package framework.web.wdm.session;

import org.openqa.selenium.WebDriver;

public class SessionStorage extends WebStorage {
  public SessionStorage(WebDriver webDriver) {
    super(webDriver, "sessionStorage");
  }
}
