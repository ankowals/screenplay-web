package screenplay.framework.actor;

import screenplay.framework.Question;

public interface AsksQuestions {
    <T> T asksFor(Question<T> question);
}
