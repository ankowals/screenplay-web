package screenplay.questions;

import framework.pom.page.BasePage;
import screenplay.abilities.BrowseTheWeb;
import framework.screenplay.Question;

public class PageProperties {

    public static <T extends BasePage> Question<String> title(Class<T> page) {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(page)
                .getTitle();
    }
}
