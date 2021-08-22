package screenplay.interactions;

import io.qameta.allure.Step;
import screenplay.framework.Interaction;
import screenplay.framework.actor.Actor;

public class FindProductDetails {

    @Step("Find Product Details of {product}")
    public static Interaction of(String product) {
        return actor -> actor.attemptsTo(
                Search.forText(product),
                ViewProductDetails.of(product));
    }
}
