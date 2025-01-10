package screenplay.saucedemo;

import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;
import pom.saucedemo.LoginPage;

public class Login {
  public static Interaction with(Credentials credentials) {
    return actor ->
        BrowseTheWeb.as(actor)
            .onPage(LoginPage.class)
            .open()
            .enterUsername(credentials.username())
            .enterPassword(credentials.password())
            .clickLogin();
  }
}
