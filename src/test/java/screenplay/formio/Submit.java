package screenplay.formio;

import framework.screenplay.Interaction;
import pom.formio.AngularFormExamplePage;
import framework.web.screenplay.BrowseTheWeb;

public class Submit {
    public static Interaction exampleForm() {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(AngularFormExamplePage.class)
                .clickSubmit();
    }
}
