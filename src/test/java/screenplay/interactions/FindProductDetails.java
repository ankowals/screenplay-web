package screenplay.interactions;

import screenplay.framework.Interaction;

public class FindProductDetails {

    public static Interaction of(String product) {
        return actor -> actor.attemptsTo(
                Search.forText(product),
                ViewProductDetails.of(product));
    }
}
