package screenplay.questions;

import io.qameta.allure.Step;
import pom.framework.page.BasePage;
import screenplay.abilities.BrowseTheWeb;
import screenplay.framework.Question;

public class PageProperties {

    @Step("Get page title")
    public static <T extends BasePage> Question<String> title(Class<T> page) {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(page)
                .getTitle();
    }
}
