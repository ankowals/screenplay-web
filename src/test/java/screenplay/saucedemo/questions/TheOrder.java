package screenplay.saucedemo.questions;

import framework.screenplay.Question;
import framework.web.screenplay.BrowseTheWeb;
import pom.saucedemo.CheckoutOverviewPage;

public class TheOrder {
  public static Question<CheckoutOverviewPage.SummaryInfo> summaryInfo() {
    return actor -> BrowseTheWeb.as(actor).onPage(CheckoutOverviewPage.class).getSummaryInfo();
  }
}
