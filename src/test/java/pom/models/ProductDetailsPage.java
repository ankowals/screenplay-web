package pom.models;

import org.jsoup.Jsoup;
import org.openqa.selenium.WebDriver;
import pom.framework.page.BasePage;
import pom.views.ProductDetailsView;
import java.util.*;

public class ProductDetailsPage extends BasePage {

    private final ProductDetailsView view  = new ProductDetailsView(driver);

    public ProductDetailsPage(WebDriver driver) { super(driver); }

    public String getPrice() {
        return view.getPriceElement().getText();
    }
    public String getShortDescription() {
        return view.getShortDescriptionElement().getText();
    }
    public List<Map<String, String>> getDataSheet() {
        List<Map<String, String>> table = new ArrayList<>();
        String source = "<table>" + view.getDataSheetTable().getElementSource() + "</table>";

        Jsoup.parseBodyFragment(source).select("tr").forEach(row -> {
            List<String> columns = row.select("td").eachText();
            table.add(Collections.singletonMap(columns.get(0), columns.get(1)));
        });

        return table;
    }
}
