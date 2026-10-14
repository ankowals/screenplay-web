package com.github.ankowals.framework.screenplay.helpers.parallel;

import com.github.ankowals.framework.screenplay.Interaction;
import com.github.ankowals.framework.screenplay.Question;
import java.util.List;

public interface OneAttemptsTo {
  void attemptsTo(Interaction... interactions);

  void attemptsTo(int parallelism, Interaction... interactions);

  @SuppressWarnings("unchecked")
  <T> List<T> asksFor(Question<T>... questions);

  @SuppressWarnings("unchecked")
  <T> List<T> asksFor(int parallelism, Question<T>... questions);
}
