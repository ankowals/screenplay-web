package com.github.ankowals.framework.web.devtools;

import java.util.Optional;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v145.emulation.Emulation;

// https://developer.chrome.com/docs/chromedriver/mobile-emulation

// usage of devTools allows us to turn it on after browser is open
// with driver options we need to do it upon browser start
public class EmulationDomain {

  private final DevTools devTools;

  EmulationDomain(DevTools devTools) {
    this.devTools = devTools;
  }

  public void emulate(MobileDevice mobileDevice) {
    this.devTools.send(
        Emulation.setDeviceMetricsOverride(
            mobileDevice.width(),
            mobileDevice.height(),
            mobileDevice.pixelRatio(),
            true,
            Optional.empty(),
            Optional.of(mobileDevice.width()),
            Optional.of(mobileDevice.height()),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()));
  }

  public void clear() {
    this.devTools.send(Emulation.clearDeviceMetricsOverride());
  }
}
