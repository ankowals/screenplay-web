package com.github.ankowals.framework.screenplay.helpers.use;

public class NoMatchingAbilityException extends RuntimeException {
  <T> NoMatchingAbilityException(Class<? extends T> ability) {
    super(
        "Actor does not have an ability [%s]. Call can() first to add an ability to an Actor."
            .formatted(ability.getName()));
  }
}
