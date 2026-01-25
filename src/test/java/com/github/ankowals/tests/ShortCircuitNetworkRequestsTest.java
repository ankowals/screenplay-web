package com.github.ankowals.tests;

import static com.github.ankowals.framework.screenplay.helpers.bdd.Bdd.*;

import com.github.ankowals.base.TestBase;
import com.github.ankowals.domain.BrowseTheWeb;
import com.github.ankowals.domain.formio.interactions.Fill;
import com.github.ankowals.domain.formio.interactions.Open;
import com.github.ankowals.domain.formio.interactions.Submit;
import com.github.ankowals.domain.formio.questions.TheExampleForm;
import com.github.ankowals.framework.screenplay.actor.Actor;
import com.github.ankowals.framework.screenplay.actor.Actors;
import com.github.ankowals.framework.screenplay.helpers.See;
import java.util.function.Predicate;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.remote.http.*;

class ShortCircuitNetworkRequestsTest extends TestBase {

  private Actor user;

  @BeforeEach
  void beforeEach() {
    this.user = Actors.withAbilities();
    this.user.can(BrowseTheWeb.with(this.browser));
  }

  @Test
  void shouldSubmitForm() throws Exception {
    given(this.user).can(BrowseTheWeb.with(this.browser));
    when(this.user)
        .attemptsTo(
            Open.browser(),
            Fill.exampleForm().firstName(RandomStringUtils.insecure().nextAlphabetic(8)),
            Submit.exampleForm());
    then(this.user)
        .should(
            See.eventually(
                TheExampleForm.submitMessage(), Matchers.containsString("Submission Complete")));
  }

  /*
  Use NetworkInterceptor to short-circuit backend requests
   */
  @Test
  @EnabledIfEnvironmentVariable(named = "BROWSER_WATCHER_ENABLED", matches = "false")
  void shouldNotifyAboutSubmissionFailure() throws Exception {
    String name = RandomStringUtils.insecure().nextAlphabetic(8);

    given(this.user).can(BrowseTheWeb.with(this.browser));

    try (NetworkInterceptor interceptor =
        new NetworkInterceptor(this.browser, this.createRouting())) {

      when(this.user)
          .attemptsTo(
              Open.browser(),
              Fill.exampleForm(formPage -> formPage.enterFirstName(name).clickSubmit()));

      then(this.user)
          .should(
              See.eventually(
                  TheExampleForm.submitMessage(),
                  Matchers.containsString(
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
