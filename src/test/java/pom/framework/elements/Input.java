package pom.framework.elements;

public interface Input extends Element {
    void clear();
    void insert(String text);
    String getText();
}
