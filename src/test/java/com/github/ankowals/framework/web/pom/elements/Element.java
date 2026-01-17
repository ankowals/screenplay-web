package com.github.ankowals.framework.web.pom.elements;

import org.openqa.selenium.WebElement;

public interface Element extends WebElement {
  WebElement getWrappedWebElement();

  String getSource();

  void scrollTo();

  boolean isInsideFrame();

  boolean isVisibleInViewport();
}
