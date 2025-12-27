package domain.saucedemo.questions;

import domain.saucedemo.pom.LoginPage;
import framework.screenplay.Question;
import framework.web.screenplay.BrowseTheWeb;

public class TheErrorMessage {
  public static Question<String> uponLogin() {
    return actor -> BrowseTheWeb.as(actor).onPage(LoginPage.class).getErrorText();
  }
}
