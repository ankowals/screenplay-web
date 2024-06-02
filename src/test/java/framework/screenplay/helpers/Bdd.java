package framework.screenplay.helpers;

import framework.screenplay.actor.Actor;

public class Bdd {
    public static <T> T given(T t) { return t; }
    public static Actor when(Actor actor) { return actor; }
    public static Actor then(Actor actor) { return actor; }
    public static <T> T and(T t) { return t; }
    public static <T> T but(T t) { return t; }
}
