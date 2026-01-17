package framework.screenplay.abilities.memory;

public class NoObjectToRecallException extends RuntimeException {
  NoObjectToRecallException(String name) {
    super(
        String.format(
            "Actor does not recall [%s]. Call remember() first to define this object in Actor's memory.",
            name));
  }
}
