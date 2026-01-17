package com.github.ankowals.domain.automationpractice.interactions;

import com.github.ankowals.framework.screenplay.Interaction;

public class Find {

  public static Interaction product(String product) {
    return actor -> actor.attemptsTo(Search.forText(product), View.product(product));
  }
}
