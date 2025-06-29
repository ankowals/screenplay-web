package framework.helpers;

import java.util.Objects;
import java.util.function.Predicate;
import org.apache.commons.lang3.StringUtils;

public class Require {

  public static <T> T notNull(T input) {
    new Validator<>(Objects::isNull, "Null input not allowed!").test(input);
    return input;
  }

  public static String notEmpty(String input) {
    Require.notNull(input);
    new Validator<>(StringUtils::isEmpty, "Empty input not allowed!").test(input);
    return input;
  }

  public static class Validator<T> {

    private final Predicate<T> predicate;
    private final String message;

    public Validator(Predicate<T> predicate, String message) {
      this.predicate = predicate;
      this.message = message;
    }

    public void test(T input) {
      if (this.predicate.test(input)) {
        throw new IllegalArgumentException(this.message);
      }
    }
  }
}
