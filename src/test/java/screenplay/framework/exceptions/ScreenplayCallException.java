package screenplay.framework.exceptions;

public class ScreenplayCallException extends RuntimeException {
    public ScreenplayCallException(String message) {
        super(message);
    }

    public ScreenplayCallException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
