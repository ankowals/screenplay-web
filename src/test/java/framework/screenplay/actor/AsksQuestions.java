package framework.screenplay.actor;

import framework.screenplay.Question;

@FunctionalInterface
public interface AsksQuestions {
    <T> T asksFor(Question<T> question);
}
