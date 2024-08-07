package tests;

import base.TestBase;
import framework.screenplay.helpers.See;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.remote.http.*;
import framework.web.screenplay.BrowseTheWeb;
import screenplay.formio.ExampleForm;
import screenplay.Open;
import screenplay.formio.FillExampleForm;
import screenplay.formio.Submit;

import java.util.function.Predicate;

import static framework.screenplay.helpers.Bdd.*;
import static org.hamcrest.CoreMatchers.containsString;
import static screenplay.PageUrl.FORM_IO_DEMO;

class AngularFormIoTest extends TestBase {

    @Tag("short-circuit-demo")
    @Test
    void shouldSubmitForm() {
            given(this.user).can(BrowseTheWeb.with(this.browser));
            when(this.user).attemptsTo(
                    Open.browser(FORM_IO_DEMO),
                    FillExampleForm.firstName(RandomStringUtils.randomAlphabetic(8)),
                    Submit.exampleForm()
            );
            then(this.user).should(See.that(ExampleForm.submitMessage(),
                    containsString("Submission Complete")));
    }

    /*
    Use NetworkInterceptor to short-circuit backend requests
     */
    @Tag("short-circuit-demo")
    @Test
    void shouldNotifyAboutSubmissionFailure() {
        try (NetworkInterceptor interceptor = new NetworkInterceptor(this.browser, this.createRouting())) {

            given(this.user).can(BrowseTheWeb.with(this.browser));
            when(this.user).attemptsTo(
                    Open.browser(FORM_IO_DEMO),
                    FillExampleForm.firstName(RandomStringUtils.randomAlphabetic(8)),
                    Submit.exampleForm()
            );
            then(this.user).should(See.that(ExampleForm.submitMessage(),
                    containsString("Please check the form and correct all errors before submitting")));
        }
    }

    //refactor to use filters instead of routing
    private Routable createRouting() {
        return Route.combine(
                Route.matching(HttpPredicates.post("example.form.io/example/submission"))
                        .to(() -> req -> new HttpResponse()
                                        .setStatus(418)
                                        .addHeader( "Access-Control-Allow-Origin", "*")
                                        .setContent(req.getContent())),

                Route.matching(HttpPredicates.get("tequila.123/entity/1"))
                        .to(() -> req -> new HttpResponse()
                                .setStatus(200)
                                .addHeader( "Access-Control-Allow-Origin", "*")
                                .setContent(Contents.utf8String("terefere"))));
    }

    static class HttpPredicates {
        public static Predicate<HttpRequest> post(String uri) {
            return request -> HttpMethod.POST == request.getMethod() && request.getUri().contains(uri);
        }

        public static Predicate<HttpRequest> get(String uri) {
            return request -> HttpMethod.GET == request.getMethod() && request.getUri().contains(uri);
        }
    }
}