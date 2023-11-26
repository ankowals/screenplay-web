package framework.web.pom.elements;

import org.openqa.selenium.WebElement;

public interface Element extends WebElement {
    WebElement getWrappedWebElement();
    String getWebElementSource();
    void scrollTo();
    boolean isInsideFrame();
}
