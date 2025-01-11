package tests;

import static framework.screenplay.helpers.Bdd.*;
import static org.hamcrest.CoreMatchers.containsString;

import base.TestBase;
import framework.screenplay.helpers.See;
import framework.web.screenplay.BrowseTheWeb;
import java.util.function.Predicate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.remote.http.*;
import screenplay.formio.ExampleForm;
import screenplay.formio.Fill;
import screenplay.formio.Open;
import screenplay.formio.Submit;

class ShortCircuitNetworkRequestsTest extends TestBase {

  @Test
  void shouldSubmitForm() throws Exception {
    given(this.user).can(BrowseTheWeb.with(this.browser));
    when(this.user)
        .attemptsTo(
            Open.browser(),
            Fill.exampleForm().firstName(RandomStringUtils.insecure().nextAlphabetic(8)),
            Submit.exampleForm());
    then(this.user)
        .should(See.that(ExampleForm.submitMessage(), containsString("Submission Complete")));
  }

  /*
  Use NetworkInterceptor to short-circuit backend requests
   */
  @Test
  void shouldNotifyAboutSubmissionFailure() throws Exception {
    String name = RandomStringUtils.insecure().nextAlphabetic(8);

    try (NetworkInterceptor interceptor =
        new NetworkInterceptor(this.browser, this.createRouting())) {

      given(this.user).can(BrowseTheWeb.with(this.browser));
      when(this.user)
          .attemptsTo(
              Open.browser(),
              Fill.exampleForm(formPage -> formPage.enterFirstName(name).clickSubmit()));
      then(this.user)
          .should(
              See.that(
                  ExampleForm.submitMessage(),
                  containsString(
                      "Please check the form and correct all errors before submitting")));
    }
  }

  private Routable createRouting() {
    return Route.combine(
        Route.matching(HttpPredicates.post("example.form.io/example/submission"))
            .to(
                () ->
                    req ->
                        new HttpResponse()
                            .setStatus(418)
                            .addHeader("Access-Control-Allow-Origin", "*")
                            .setContent(req.getContent())),
        Route.matching(HttpPredicates.get("tequila.123/entity/1"))
            .to(
                () ->
                    req ->
                        new HttpResponse()
                            .setStatus(200)
                            .addHeader("Access-Control-Allow-Origin", "*")
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
