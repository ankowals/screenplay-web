package domain.saucedemo.questions;

import domain.BrowseTheWeb;
import domain.saucedemo.pom.CheckoutOverviewPage;
import framework.screenplay.Question;

public class TheOrder {
  public static Question<CheckoutOverviewPage.SummaryInfo> summaryInfo() {
    return actor -> BrowseTheWeb.as(actor).onPage(CheckoutOverviewPage.class).getSummaryInfo();
  }
}
