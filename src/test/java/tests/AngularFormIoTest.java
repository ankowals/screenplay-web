package tests;

import base.TestBase;
import mocks.HttpRequests;
import mocks.Mock;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.remote.http.Route;
import screenplay.BrowseTheWeb;
import screenplay.formio.ExampleForm;
import screenplay.Open;
import screenplay.formio.FillExampleForm;
import screenplay.formio.Submit;

import static framework.screenplay.helpers.Bdd.*;
import static framework.screenplay.helpers.SeeThat.seeThat;
import static mocks.Mock.I_AM_A_TEA_POT;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.containsString;
import static screenplay.PageUrl.FORM_IO_DEMO;

public class AngularFormIoTest extends TestBase {

    @Test
    void shouldSubmitForm() {
            given(user).can(BrowseTheWeb.with(browser));
            when(user).attemptsTo(
                    Open.browser(FORM_IO_DEMO),
                    FillExampleForm.firstName(randomAlphabetic(8)),
                    Submit.exampleForm()
            );
            then(user).should(seeThat(ExampleForm.submitMessage(),
                    containsString("Submission Complete")));
    }

    /*
    Use NetworkInterceptor to short-circuit backend requests
     */
    @Test
    void shouldNotifyAboutSubmissionFailure() throws IllegalAccessException {
        try (NetworkInterceptor interceptor = new NetworkInterceptor(this.driverAugmenter.augment(this.browser),
                Route.combine(
                        Route.matching(HttpRequests.post("example.form.io/example/submission"))
                        .to(() -> new Mock().response(I_AM_A_TEA_POT))))) {

            given(user).can(BrowseTheWeb.with(browser));
            when(user).attemptsTo(
                    Open.browser(FORM_IO_DEMO),
                    FillExampleForm.firstName(randomAlphabetic(8)),
                    Submit.exampleForm()
            );
            then(user).should(seeThat(ExampleForm.submitMessage(),
                    containsString("Please check the form and correct all errors before submitting")));
        }
    }

}