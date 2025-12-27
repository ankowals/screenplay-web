package domain.automationpractice.interactions;

import framework.screenplay.Interaction;

public class Find {

  public static Interaction product(String product) {
    return actor -> actor.attemptsTo(Search.forText(product), View.product(product));
  }
}
