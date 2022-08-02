package screenplay.questions;

import pom.framework.page.BasePage;
import screenplay.abilities.BrowseTheWeb;
import screenplay.framework.Question;

public class PageProperties {

    public static <T extends BasePage> Question<String> title(Class<T> page) {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(page)
                .getTitle();
    }
}
