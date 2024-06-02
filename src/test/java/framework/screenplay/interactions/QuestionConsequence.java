package framework.screenplay.interactions;

import framework.screenplay.Consequence;
import framework.screenplay.Question;
import framework.screenplay.actor.Actor;
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
        actor.checksThat(this.question.answeredBy(actor), this.matcher);
    }
}
