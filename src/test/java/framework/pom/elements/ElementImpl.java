package framework.pom.elements;

import org.openqa.selenium.*;

import java.util.List;

public class ElementImpl implements Element {

    protected final WebElement element;
    protected final JavascriptExecutor js;

    public ElementImpl(final WebElement element) {
        this.element = element;
        this.js = (JavascriptExecutor) ((WrapsDriver) getWrappedElement()).getWrappedDriver();
    }

    @Override
    public void click() {
        element.click();
    }

    @Override
    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return element.getLocation();
    }

    @Override
    public Dimension getSize() {
        return element.getSize();
    }

    @Override
    public Rectangle getRect() {
        return element.getRect();
    }

    @Override
    public String getCssValue(String propertyName) {
        return element.getCssValue(propertyName);
    }

    @Override
    public void submit() {
        element.submit();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        if (isDisplayed())
            element.sendKeys(charSequences);
    }

    @Override
    public String getText() {
        isDisplayed();
        return element.getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return element.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return element.findElement(by);
    }

    @Override
    public void clear() {
        if(isDisplayed())
            element.clear();
    }

    @Override
    public String getTagName() {
        return element.getTagName();
    }

    @Override
    public String getAttribute(String s) {
        return element.getAttribute(s);
    }

    @Override
    public boolean isSelected() {
        return element.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return element.isEnabled();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return element.getScreenshotAs(target);
    }

    public WebElement getWrappedElement() {
        return element;
    }

    public String getElementSource() {
            return (String) js.executeScript("return arguments[0].innerHTML;", element);
    }

    public void scrollTo() {
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected boolean isAttachedToDOM() {
        try {
            element.isEnabled();
        } catch (StaleElementReferenceException e) {
            return false;
        }
        return true;
    }

    public static ElementImpl of(WebElement element) {
        return new ElementImpl(element);
    }
}
