package pom.saucedemo;

import framework.web.pom.page.BasePage;
import org.openqa.selenium.WebDriver;

public class CheckoutOverviewPage extends BasePage {

  HeaderView headerView = new HeaderView(this.driver);

  public CheckoutOverviewPage(WebDriver driver) {
    super(driver);
  }

  @Override
  public String getTitle() {
    return this.headerView.getTitleElement().getText();
  }
}
