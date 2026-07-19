package com.github.ankowals.domain.automationpractice.questions;

import com.github.ankowals.abilities.BrowseTheWeb;
import com.github.ankowals.framework.screenplay.Question;
import com.github.ankowals.framework.web.pom.page.BasePage;
import java.util.function.Function;
import org.openqa.selenium.WebDriver;

public class ThePage {
  public static <T extends BasePage> Question<String> title(Function<WebDriver, T> pageMapper) {
    return actor -> BrowseTheWeb.as(actor).on(pageMapper).getTitle();
  }
}
