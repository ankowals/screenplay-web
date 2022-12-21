package pom.parasoft.models;

import framework.pom.page.BasePage;
import org.openqa.selenium.WebDriver;
import pom.parasoft.views.ParasoftProductsView;

public class ParasoftProductsPage extends BasePage {

    private final ParasoftProductsView view = new ParasoftProductsView(driver);

    public ParasoftProductsPage(WebDriver driver) { super(driver); }

    public String getTitle() {
        return view.getTitle().getText();
    }
}
