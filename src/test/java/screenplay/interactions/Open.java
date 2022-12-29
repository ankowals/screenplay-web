package screenplay.interactions;

import framework.web.pom.page.BasePage;
import screenplay.PageUrl;
import screenplay.abilities.BrowseTheWeb;
import framework.screenplay.Interaction;

public class Open {

    public static Interaction browserAt(String url) {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(PageUrl.getMapping(url))
                .open(url);
    }

    public static Interaction browserAt(String url, Class<? extends BasePage> page) {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(page)
                .open(url);
    }
}
