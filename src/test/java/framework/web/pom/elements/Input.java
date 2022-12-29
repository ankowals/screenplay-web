package framework.web.pom.elements;

public interface Input extends Element {
    void clear();
    void insert(String text);
    String getText();
}
