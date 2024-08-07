package screenplay.automationpractice.questions;

import framework.web.pom.page.BasePage;
import framework.web.screenplay.BrowseTheWeb;
import framework.screenplay.Question;

public class PageProperties {

    public static <T extends BasePage> Question<String> title(Class<T> page) {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(page)
                .getTitle();
    }
}
