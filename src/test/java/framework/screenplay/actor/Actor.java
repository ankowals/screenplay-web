package framework.screenplay.actor;

import framework.screenplay.*;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.SoftAssertions;
import org.hamcrest.Matcher;
import framework.screenplay.exceptions.ScreenplayCallException;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.HamcrestCondition.matching;
import static framework.screenplay.helpers.SeeThat.seeThat;
import static framework.screenplay.helpers.ThrowingConsumer.unchecked;

public class Actor implements PerformsInteractions, PerformsChecks, ManagesFacts, ManagesAbilities, ManagesIntegrations, AsksQuestions {

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
        abilities.put(doSomething.getClass(), doSomething);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Ability> T using(Class<? extends T> ability) {
        return (T) abilities.get(ability);
    }

    @Override
    public PerformsInteractions attemptsTo(Interaction... interactions) {
        Arrays.asList(interactions).forEach(unchecked(interaction -> interaction.performAs(this)));
        return this;
    }

    @Override
    public PerformsInteractions wasAbleTo(Interaction... interactions) {
        return attemptsTo(interactions);
    }

    @Override
    public ManagesFacts has(Fact... facts) {
        Arrays.asList(facts).forEach(unchecked(fact -> fact.setupFor(this)));
        return this;
    }

    @Override
    public ManagesFacts was(Fact... facts) {
        return has(facts);
    }

    @Override
    public ManagesFacts is(Fact... facts) {
        return has(facts);
    }

    @Override
    public <T> T asksFor(Question<T> question) {
        return question.answeredBy(this);
    }

    @SafeVarargs
    @Override
    public final <T> void checksThat(T actual, Matcher<? super T>... matchers) {
        Arrays.asList(matchers).forEach(matcher -> should(seeThat(actual)).is(matching(matcher)));
    }

    @Override
    public <T> void expects(Question<T> question, Matcher<? super T> matcher) {
        checksThat(question.answeredBy(this), matcher);
    }

    @SafeVarargs
    @Override
    public final <T> PerformsChecks should(T actual, Matcher<? super T>... matchers) {
        checksThat(actual, matchers);
        return this;
    }

    @Override
    public PerformsChecks should(Consequence consequence) {
        consequence.evaluateFor(this);
        return this;
    }

    @Override
    public <T, E extends AbstractObjectAssert<E, T>> E should(Question<T> question) {
        return should(seeThat(question.answeredBy(this)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T, E extends AbstractObjectAssert<E, T>> E should(T actual) {
        if(softAssertions != null)
            return (E) softAssertions.assertThat(actual);

        return (E) assertThat(actual);
    }

    @Override
    public <T> T should (Integration<T> integration) {
        return integration.integratedBy(this);
    }

    public <T> void remember(String name, T value) {
        memory.put(name, value);
    }
    public Object recall(String name) {
        if(memory.get(name) == null)
            throw new ScreenplayCallException("Actor does not recall parameter [" + name + "].Call remember() before to define this object in Actor's memory.");
        return memory.get(name);
    }
}
