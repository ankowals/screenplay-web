package com.github.ankowals.framework.web.pom.elements.common;

import com.github.ankowals.framework.web.pom.elements.Input;
import java.util.Objects;
import org.openqa.selenium.*;

@SuppressWarnings("NullableProblems")
public class InputImpl extends ElementImpl implements Input {

  public InputImpl(WebElement webElement) {
    super(webElement);
  }

  @Override
  public void insert(String text) {
    super.clear();
    super.sendKeys(text);
  }

  @Override
  public String getText() {
    return Objects.requireNonNull(super.getAttribute("value"));
  }

  public static InputImpl of(WebElement webElement) {
    return new InputImpl(webElement);
  }
}
