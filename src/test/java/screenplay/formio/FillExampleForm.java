package screenplay.formio;

import framework.screenplay.Interaction;
import pom.formio.AngularFormExamplePage;
import framework.web.screenplay.BrowseTheWeb;

public class FillExampleForm {
    public static Interaction firstName(String firstName) {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(AngularFormExamplePage.class)
                .enterFirstName(firstName);
    }
}
