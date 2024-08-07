package screenplay.formio;

import framework.screenplay.Question;
import pom.formio.AngularFormExamplePage;
import framework.web.screenplay.BrowseTheWeb;

public class ExampleForm {

    public static Question<String> submitMessage() {
        return actor -> BrowseTheWeb.as(actor)
                .onPage(AngularFormExamplePage.class)
                .getSubmitMessageText();
    }
}
