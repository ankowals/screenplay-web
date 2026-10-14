package com.github.ankowals.framework.screenplay.helpers.parallel;

import com.github.ankowals.framework.screenplay.Interaction;
import com.github.ankowals.framework.screenplay.Question;
import java.util.List;
import java.util.function.Supplier;

public interface EachAttemptsTo {

  @SuppressWarnings("unchecked")
  void eachAttemptsTo(Supplier<Interaction>... suppliers);

  @SuppressWarnings("unchecked")
  void eachAttemptsTo(int parallelism, Supplier<Interaction>... suppliers);

  @SuppressWarnings("unchecked")
  <T> List<T> eachAsksFor(Supplier<Question<T>>... suppliers);

  @SuppressWarnings("unchecked")
  <T> List<T> eachAsksFor(int parallelism, Supplier<Question<T>>... suppliers);
}
