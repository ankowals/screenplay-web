package framework.screenplay.actor;

import framework.screenplay.*;
import framework.screenplay.helpers.See;
import org.apache.commons.lang3.function.Failable;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matcher;
import framework.screenplay.exceptions.ScreenplayCallException;

import java.util.*;

import static org.assertj.core.api.HamcrestCondition.matching;

public class Actor implements PerformsInteractions, PerformsChecks, ManagesFacts, ManagesAbilities, AsksQuestions {

    private SoftAssertions softAssertions;

    @SuppressWarnings("rawtypes")
    protected final Map<Class, Ability> abilities = new HashMap<>();
    protected final Map<String, Object> memory = new HashMap<>();

    public Actor() {}
    public Actor(SoftAssertions softAssertions){
        this.softAssertions = softAssertions;
    }

    @Override
    public <T extends Ability> Actor can(T doSomething) {
        this.abilities.put(doSomething.getClass(), doSomething);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Ability> T using(Class<? extends T> ability) {
        return (T) this.abilities.get(ability);
    }

    @Override
    public PerformsInteractions attemptsTo(Interaction... interactions) {
        Failable.stream(Arrays.asList(interactions)).forEach(interaction -> interaction.performAs(this));
        return this;
    }

    @Override
    public PerformsInteractions wasAbleTo(Interaction... interactions) {
        return this.attemptsTo(interactions);
    }

    @Override
    public ManagesFacts has(Fact... facts) {
        Failable.stream(Arrays.asList(facts)).forEach(fact -> fact.setupFor(this));
        return this;
    }

    @Override
    public ManagesFacts was(Fact... facts) {
        return this.has(facts);
    }

    @Override
    public ManagesFacts is(Fact... facts) {
        return this.has(facts);
    }

    @Override
    public <T> T asksFor(Question<T> question) {
        return question.answeredBy(this);
    }

    @SafeVarargs
    @Override
    public final <T> void checksThat(T actual, Matcher<? super T>... matchers) {
        Arrays.asList(matchers).forEach(matcher -> should(See.that(actual)).is(matching(matcher)));
    }

    @Override
    public <T> void expects(Question<T> question, Matcher<? super T> matcher) {
        this.checksThat(question.answeredBy(this), matcher);
    }

    @SafeVarargs
    @Override
    public final <T> PerformsChecks should(T actual, Matcher<? super T>... matchers) {
        this.checksThat(actual, matchers);
        return this;
    }

    @Override
    public PerformsChecks should(Consequence consequence) {
        consequence.evaluateFor(this);
        return this;
    }

    @Override
    public <T, E extends AbstractObjectAssert<E, T>> E should(Question<T> question) {
        return this.should(See.that(question.answeredBy(this)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, E extends AbstractObjectAssert<E, T>> E should(T actual) {
        if (this.softAssertions != null)
            return (E) this.softAssertions.assertThat(actual);

        return (E) Assertions.assertThat(actual);
    }

    public <T> void remember(String name, T value) {
        this.memory.put(name, value);
    }

    public Object recall(String name) {
        if (this.memory.get(name) == null)
            throw new ScreenplayCallException("Actor does not recall parameter [" + name + "]." +
                    "Call remember() before to define this object in Actor's memory.");

        return this.memory.get(name);
    }
}
