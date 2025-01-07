package screenplay.automationpractice.interactions;

import framework.screenplay.Interaction;
import framework.screenplay.actor.Actor;

public class Find {

  public static Interaction<Actor> productDetails(String product) {
    return actor -> actor.attemptsTo(Search.forText(product), View.productDetails(product));
  }
}
