package com.github.ankowals.framework.web.wdm.session;

import java.util.List;
import java.util.Optional;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v143.network.Network;
import org.openqa.selenium.devtools.v143.network.model.Cookie;
import org.openqa.selenium.devtools.v143.network.model.CookieParam;
import org.openqa.selenium.devtools.v143.network.model.TimeSinceEpoch;

public class Cookies {
  private final List<Cookie> cookies;
  private final DevTools devTools;

  public Cookies(WebDriver webDriver) {
    this.devTools = this.toDevTools(webDriver);
    // alternative Storage.getCookies(Optional.empty());
    this.cookies = this.devTools.send(Network.getAllCookies());
  }

  // clear not needed, assumed only called in new browser
  public void set() {
    List<CookieParam> cookieParams =
        this.cookies.stream()
            .map(
                cookie ->
                    new CookieParam(
                        cookie.getName(),
                        cookie.getValue(),
                        Optional.empty(),
                        Optional.of(cookie.getDomain()),
                        Optional.of(cookie.getPath()),
                        Optional.of(cookie.getSecure()),
                        Optional.of(cookie.getHttpOnly()),
                        cookie.getSameSite(),
                        Optional.of(new TimeSinceEpoch(cookie.getExpires())),
                        Optional.of(cookie.getPriority()),
                        Optional.of(cookie.getSameParty()),
                        Optional.of(cookie.getSourceScheme()),
                        Optional.of(cookie.getSourcePort()),
                        cookie.getPartitionKey()))
            .toList();

    // alternative Storage.setCookies(cookieParams);
    // this.devTools.send(Network.clearBrowserCookies());
    this.devTools.send(Network.setCookies(cookieParams));
  }

  private DevTools toDevTools(WebDriver webDriver) {
    DevTools dt = ((HasDevTools) webDriver).getDevTools();
    dt.createSessionIfThereIsNotOne();
    dt.send(
        Network.enable(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()));

    return dt;
  }
}
