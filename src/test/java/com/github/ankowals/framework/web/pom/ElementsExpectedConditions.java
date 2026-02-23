package com.github.ankowals.framework.web.pom;

import com.github.ankowals.framework.web.pom.elements.common.ElementImpl;
import java.util.Map;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

// https://playwright.dev/docs/actionability
public class ElementsExpectedConditions {

  public static ExpectedCondition<WebElement> elementToBeClickable(final By locator) {
    return driver -> {
      Objects.requireNonNull(driver);

      // visible, enabled & in viewport
      ElementImpl.of(ExpectedConditions.elementToBeClickable(locator).apply(driver)).scrollTo();

      // stable & not covered
      ExpectedConditions.and(
              ElementsExpectedConditions.hasFinishedAnimating(
                  ExpectedConditions.elementToBeClickable(locator).apply(driver)),
              ElementsExpectedConditions.isInteractable(
                  ExpectedConditions.elementToBeClickable(locator).apply(driver)))
          .apply(driver);

      return ExpectedConditions.elementToBeClickable(locator).apply(driver);
    };
  }

  public static ExpectedCondition<Boolean> hasFinishedAnimating(WebElement webElement) {
    return driver -> !ElementsExpectedConditions.isAnimating(driver, webElement);
  }

  public static ExpectedCondition<Boolean> isInteractable(WebElement webElement) {
    return driver -> {
      String script =
          """
                    return (function (element) {
                        const rect = element.getBoundingClientRect();
                        const x = rect.left + rect.width / 2;
                        const y = rect.top + rect.height / 2;
                        let elementFromPoint = document.elementFromPoint(x, y);
                        return element ==  elementFromPoint || element.contains(elementFromPoint);
                    })(arguments[0])
                    """;
      return (Boolean) ((JavascriptExecutor) driver).executeScript(script, webElement);
    };
  }

  // script source https://github.com/selenide/selenide/blob/main/src/main/resources/animation.js
  @SuppressWarnings("unchecked")
  private static Boolean isAnimating(WebDriver webDriver, WebElement webElement) {
    String script =
        """
                      return (function (element) {
                        function isEqual(rect1, rect2) {
                          return rect1.x === rect2.x &&
                            rect1.y === rect2.y &&
                            rect1.width === rect2.width &&
                            rect1.height === rect2.height;
                        }

                        // requestAnimationFrame() calls are paused in most browsers
                        // when running in background tabs or hidden <iframe>s,
                        // in order to improve performance and battery life.
                        if (document.visibilityState === 'hidden') {
                          return {
                            animating: false,
                            error: 'You are checking for animations on an inactive(background) tab. It is impossible to check for animations on inactive tab.'
                          };
                        }

                        // wait for two consecutive frames to make sure there are no animations
                        return new Promise((resolve) => {
                          window.requestAnimationFrame(() => {
                            const rect1 = element.getBoundingClientRect();
                            window.requestAnimationFrame(() => {
                              const rect2 = element.getBoundingClientRect();
                              resolve({animating: !isEqual(rect1, rect2), error: null})
                            });
                          });
                        });
                      })(arguments[0])
                      """;
    Map<String, Object> result =
        (Map<String, Object>) ((JavascriptExecutor) webDriver).executeScript(script, webElement);
    return (Boolean) result.get("animating");
  }
}
