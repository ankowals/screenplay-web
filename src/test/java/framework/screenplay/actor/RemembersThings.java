package framework.screenplay.actor;

import framework.screenplay.Question;

public interface RemembersThings {
    <T> void remember(String name, T value);
    <T> void remember(String name, Question<T> question);
    <T> T recall(String name);
}
