package domain.saucedemo.questions;

import domain.BrowseTheWeb;
import domain.saucedemo.pom.LoginPage;
import framework.screenplay.Question;

public class TheErrorMessage {
  public static Question<String> uponLogin() {
    return actor -> BrowseTheWeb.as(actor).onPage(LoginPage.class).getErrorText();
  }
}
