package com.github.ankowals.domain.saucedemo.questions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.saucedemo.pom.LoginPage;
import com.github.ankowals.framework.screenplay.Question;

public class TheErrorMessage {
  public static Question<String> uponLogin() {
    return actor -> BrowseTheWeb.as(actor).onPage(LoginPage.class).getErrorText();
  }
}
