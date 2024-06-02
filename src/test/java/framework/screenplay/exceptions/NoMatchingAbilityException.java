package framework.screenplay.exceptions;

public class NoMatchingAbilityException extends RuntimeException {
    public <T> NoMatchingAbilityException(Class<? extends T> ability) {
        super(String.format("Actor does not have ability [%s]. Call can() first to add an ability to an Actor.", ability.getName()));
    }
}
