package domain.saucedemo.questions;

import domain.saucedemo.pom.CheckoutOverviewPage;
import framework.screenplay.Question;
import framework.web.screenplay.BrowseTheWeb;

public class TheOrder {
  public static Question<CheckoutOverviewPage.SummaryInfo> summaryInfo() {
    return actor -> BrowseTheWeb.as(actor).onPage(CheckoutOverviewPage.class).getSummaryInfo();
  }
}
