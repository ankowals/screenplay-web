package screenplay;

import framework.web.pom.page.BasePage;
import framework.screenplay.Interaction;
import framework.web.screenplay.BrowseTheWeb;

public class Open {

    public static Interaction browser(String url) {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(PageUrl.getMapping(url))
                .open(url);
    }

    public static Interaction browser(String url, Class<? extends BasePage> page) {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(page)
                .open(url);
    }
}
