package framework.screenplay.exceptions;

public class NoObjectToRecallException extends RuntimeException {
    public NoObjectToRecallException(String name) {
        super(String.format("Actor does not recall [%s]. Call remember() first to define this object in Actor's memory.", name));
    }
}
