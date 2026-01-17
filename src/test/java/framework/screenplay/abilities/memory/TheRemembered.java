package framework.screenplay.abilities.memory;

import framework.screenplay.Question;
import framework.screenplay.helpers.use.UseAbility;

public class TheRemembered {
  public static Question<Memory> memories() {
    return actor -> UseAbility.of(actor).to(RememberThings.class).memory().recall();
  }

  public static <T> Question<T> valueOf(Memory.Key<T> key) {
    return TheRemembered.valueOf(key.name(), key.type());
  }

  public static <T> Question<T> valueOf(String key, Class<T> type) {
    return actor -> {
      T value = UseAbility.of(actor).to(RememberThings.class).memory().recall(key, type);

      if (value == null) {
        throw new NoObjectToRecallException(key);
      }

      return value;
    };
  }

  public static <T> Question<T> valueOf(Memory.Key<T> key, Question<T> question) {
    return TheRemembered.valueOf(key.name(), key.type(), question);
  }

  public static <T> Question<T> valueOf(String key, Class<T> type, Question<T> question) {
    return actor -> {
      try {
        return actor.asksFor(TheRemembered.valueOf(key, type));
      } catch (NoObjectToRecallException noObjectToRecallException) { // NOSONAR
        T answer = question.answeredBy(actor);

        if (answer != null) {
          actor.attemptsTo(RememberThat.valueOf(key).is(answer));
        }

        return answer;
      }
    };
  }
}
