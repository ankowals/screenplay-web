package framework.screenplay.actor.use;

class NoMatchingAbilityException extends RuntimeException {
  <T> NoMatchingAbilityException(Class<? extends T> ability) {
    super(
        String.format(
            "Actor does not have an ability [%s]. Call can() first to add an ability to an Actor.",
            ability.getName()));
  }
}
