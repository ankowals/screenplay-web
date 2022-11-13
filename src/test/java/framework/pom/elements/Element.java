package framework.pom.elements;

import org.openqa.selenium.WebElement;

public interface Element extends WebElement {
    WebElement getWrappedElement();
}
