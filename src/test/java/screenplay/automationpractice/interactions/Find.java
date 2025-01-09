package screenplay.automationpractice.interactions;

import framework.screenplay.Interaction;

public class Find {

  public static Interaction productDetails(String product) {
    return actor -> actor.attemptsTo(Search.forText(product), View.productDetails(product));
  }
}
