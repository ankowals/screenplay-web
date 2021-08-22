package screenplay.framework;

import screenplay.framework.actor.Actor;
import org.hamcrest.Matcher;

public class QuestionConsequence<T> implements Consequence {

    private final Question<T> question;
    private final Matcher<? super T> matcher;

    public QuestionConsequence(Question<T> question, Matcher<? super T> matcher){
        this.question = question;
        this.matcher = matcher;
    }

    @Override
    public void evaluateFor(Actor actor) {
        actor.checksThat(question.answeredBy(actor), matcher);
    }
}
