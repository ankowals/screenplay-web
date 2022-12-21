package screenplay.automationpractice.interactions;

import framework.screenplay.Interaction;

public class FindProductDetails {

    public static Interaction of(String product) {
        return actor -> actor.attemptsTo(
                Search.forText(product),
                ViewProductDetails.of(product));
    }
}
