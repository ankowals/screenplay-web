package com.github.ankowals.framework.screenplay.abilities.memory;

import com.github.ankowals.framework.screenplay.Question;

public class TheRemembered {
  public static Question<Memory> memories() {
    return actor -> RememberThings.as(actor).recall();
  }

  public static <T> Question<T> valueOf(Memory.Key<T> key) {
    return TheRemembered.valueOf(key.name(), key.type());
  }

  public static <T> Question<T> valueOf(String key, Class<T> type) {
    return actor -> {
      T value = RememberThings.as(actor).recall(key, type);

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
