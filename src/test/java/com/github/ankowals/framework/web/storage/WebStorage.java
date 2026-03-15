package com.github.ankowals.framework.web.storage;

import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

// would be better to use devTools
public abstract class WebStorage {
  private final Map<String, String> storage;
  private final JavascriptExecutor jsExecutor;
  private final String type;

  WebStorage(WebDriver webDriver, String type) {
    this.jsExecutor = (JavascriptExecutor) webDriver;
    this.type = type;
    this.storage = this.get();
  }

  // clear not needed, assumed called only in new browser
  public void set() {
    this.storage.forEach(this::setItem);
  }

  public Map<String, String> get() {
    Map<String, String> map = new HashMap<>();

    for (int i = 0; i < this.getLength(); i++) {
      String key = this.getKey(i);
      map.put(key, this.getItem(key));
    }

    return map;
  }

  public String getItem(String key) {
    return (String)
        this.jsExecutor.executeScript("return window.%s.getItem('%s');".formatted(this.type, key));
  }

  private String getKey(int key) {
    return (String)
        this.jsExecutor.executeScript("return window.%s.key('%s')".formatted(this.type, key));
  }

  private Long getLength() {
    return (Long) this.jsExecutor.executeScript("return window.%s.length;".formatted(this.type));
  }

  private void setItem(String item, String value) {
    this.jsExecutor.executeScript(
        "window.%s.setItem('%s', '%s');".formatted(this.type, item, value));
  }
}
