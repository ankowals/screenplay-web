package com.github.ankowals.domain.saucedemo.questions;

import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.saucedemo.pom.CheckoutOverviewPage;
import com.github.ankowals.framework.screenplay.Question;

public class TheOrder {
  public static Question<CheckoutOverviewPage.SummaryInfo> summaryInfo() {
    return actor -> BrowseTheWeb.as(actor).onPage(CheckoutOverviewPage.class).getSummaryInfo();
  }
}
