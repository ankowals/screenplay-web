package framework.screenplay.actor;

import framework.screenplay.Question;

public interface RemembersThings {
  <T> void remembers(String name, T value);

  <T> void remembers(String name, Question<T> question);

  <T> T recall(String name);
}
