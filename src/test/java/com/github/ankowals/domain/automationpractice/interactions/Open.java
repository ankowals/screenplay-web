package com.github.ankowals.domain.automationpractice.interactions;

import com.github.ankowals.abilities.BrowseTheWeb;
import com.github.ankowals.domain.automationpractice.pom.models.AutomationPracticeHomePage;
import com.github.ankowals.framework.screenplay.Interaction;
import com.github.ankowals.framework.web.pom.page.BasePage;
import java.util.function.Function;
import org.openqa.selenium.WebDriver;

public class Open {

  public static Interaction automationPractice() {
    return actor -> BrowseTheWeb.as(actor).on(AutomationPracticeHomePage::new).open();
  }

  public static <T extends BasePage> Interaction browser(
      String url, Function<WebDriver, T> pageMapper) {
    return actor -> BrowseTheWeb.as(actor).on(pageMapper).open(url);
  }
}
