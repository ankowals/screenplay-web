package com.github.ankowals.domain.automationpractice.pom.models;

import com.github.ankowals.domain.automationpractice.pom.views.ProductDetailsView;
import com.github.ankowals.framework.web.pom.page.BasePage;
import java.util.*;
import org.jsoup.Jsoup;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {

  private final ProductDetailsView view = new ProductDetailsView(this.driver);

  public ProductDetailsPage(WebDriver driver) {
    super(driver);
  }

  public String getPrice() {
    return this.view.priceElement().getText();
  }

  public String getShortDescription() {
    return this.view.shortDescriptionElement().getText();
  }

  public List<Map<String, String>> getDataSheet() {
    List<Map<String, String>> table = new ArrayList<>();
    String source = "<table>" + this.view.dataSheetTable().getSource() + "</table>";

    Jsoup.parseBodyFragment(source)
        .select("tr")
        .forEach(
            row -> {
              List<String> columns = row.select("td").eachText();
              table.add(Collections.singletonMap(columns.get(0), columns.get(1)));
            });

    return table;
  }
}
