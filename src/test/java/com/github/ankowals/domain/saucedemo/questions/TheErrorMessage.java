package com.github.ankowals.domain.saucedemo.questions;

import com.github.ankowals.abilities.BrowseTheWeb;
import com.github.ankowals.domain.saucedemo.pom.LoginPage;
import com.github.ankowals.framework.screenplay.Question;

public class TheErrorMessage {
  public static Question<String> uponLogin() {
    return actor -> BrowseTheWeb.as(actor).on(LoginPage::new).getErrorText();
  }
}
