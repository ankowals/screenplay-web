package framework.web.pom.elements;

import org.openqa.selenium.*;

import java.util.List;

public class ElementImpl implements Element {

    protected final WebElement element;
    protected final JavascriptExecutor js;

    public ElementImpl(final WebElement element) {
        this.element = element;
        this.js = (JavascriptExecutor) ((WrapsDriver) this.getWrappedElement()).getWrappedDriver();
    }

    @Override
    public void click() {
        this.element.click();
    }

    @Override
    public boolean isDisplayed() {
        return this.element.isDisplayed();
    }

    @Override
    public Point getLocation() {
        return this.element.getLocation();
    }

    @Override
    public Dimension getSize() {
        return this.element.getSize();
    }

    @Override
    public Rectangle getRect() {
        return this.element.getRect();
    }

    @Override
    public String getCssValue(String propertyName) {
        return this.element.getCssValue(propertyName);
    }

    @Override
    public void submit() {
        this.element.submit();
    }

    @Override
    public void sendKeys(CharSequence... charSequences) {
        if (isDisplayed())
            this.element.sendKeys(charSequences);
    }

    @Override
    public String getText() {
        isDisplayed();
        return this.element.getText();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return this.element.findElements(by);
    }

    @Override
    public WebElement findElement(By by) {
        return this.element.findElement(by);
    }

    @Override
    public void clear() {
        if(this.isDisplayed())
            this.element.clear();
    }

    @Override
    public String getTagName() {
        return this.element.getTagName();
    }

    @Override
    public String getAttribute(String s) {
        return this.element.getAttribute(s);
    }

    @Override
    public boolean isSelected() {
        return this.element.isSelected();
    }

    @Override
    public boolean isEnabled() {
        return this.element.isEnabled();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return this.element.getScreenshotAs(target);
    }

    public WebElement getWrappedElement() {
        return this.element;
    }

    public String getElementSource() {
            return (String) this.js.executeScript("return arguments[0].innerHTML;", this.element);
    }

    public void scrollTo() {
        this.js.executeScript("arguments[0].scrollIntoView(true);", this.element);
    }

    protected boolean isAttachedToDOM() {
        try {
            this.element.isEnabled();
        } catch (StaleElementReferenceException e) {
            return false;
        }
        return true;
    }

    protected boolean isInsideFrame() {
        return this.js.executeScript("return window.frameElement") != null;
    }

    public static ElementImpl of(WebElement element) {
        return new ElementImpl(element);
    }
}
