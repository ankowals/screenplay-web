package screenplay.saucedemo;

import framework.screenplay.Question;
import framework.web.screenplay.BrowseTheWeb;
import pom.saucedemo.LoginPage;

public class TheErrorMessage {
  public static Question<String> uponLogin() {
    return actor -> BrowseTheWeb.as(actor).onPage(LoginPage.class).getErrorText();
  }
}
