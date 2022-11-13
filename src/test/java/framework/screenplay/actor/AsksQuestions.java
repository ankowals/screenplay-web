package framework.screenplay.actor;

import framework.screenplay.Question;

public interface AsksQuestions {
    <T> T asksFor(Question<T> question);
}
