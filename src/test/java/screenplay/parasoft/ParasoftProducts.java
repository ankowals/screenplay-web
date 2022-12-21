package screenplay.parasoft;

import framework.pom.page.BasePage;
import framework.screenplay.Question;
import pom.parasoft.models.ParasoftProductsPage;
import screenplay.abilities.BrowseTheWeb;

public class ParasoftProducts {
    public static <T extends BasePage> Question<String> title() {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(ParasoftProductsPage.class)
                .getTitle();
    }
}
