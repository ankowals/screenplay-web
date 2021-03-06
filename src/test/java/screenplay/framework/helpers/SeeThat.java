package screenplay.framework.helpers;

import screenplay.framework.*;
import org.hamcrest.Matcher;

public class SeeThat {

    public static <T> Consequence seeThat (Question<T> question, Matcher<? super T> matcher) {
        return new <T>QuestionConsequence<T>(question, matcher);
    }
    public static Consequence seeThat (Consequence consequence) {
        return consequence;
    }
    public static <T> T seeThat (T actual) { return actual; }
    public static <T> Question<T> seeThat (Question<T> question) {
        return question;
    }
}
