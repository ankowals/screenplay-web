package framework.web.pom.elements.common;

import framework.web.pom.elements.Element;
import java.util.List;
import java.util.Optional;
import org.openqa.selenium.*;

@SuppressWarnings("NullableProblems")
public class ElementImpl implements Element {

  protected final WebElement webElement;
  protected final JavascriptExecutor js;

  public ElementImpl(final WebElement webElement) {
    this.webElement = webElement;
    this.js = (JavascriptExecutor) ((WrapsDriver) this.getWrappedWebElement()).getWrappedDriver();
  }

  @Override
  public void click() {
    this.webElement.click();
  }

  @Override
  public boolean isDisplayed() {
    return this.webElement.isDisplayed();
  }

  @Override
  public Point getLocation() {
    return this.webElement.getLocation();
  }

  @Override
  public Dimension getSize() {
    return this.webElement.getSize();
  }

  @Override
  public Rectangle getRect() {
    return this.webElement.getRect();
  }

  @Override
  public String getCssValue(String propertyName) {
    return this.webElement.getCssValue(propertyName);
  }

  @Override
  public void submit() {
    this.webElement.submit();
  }

  @Override
  public void sendKeys(CharSequence... charSequences) {
    this.webElement.sendKeys(charSequences);
  }

  @Override
  public String getText() {
    return this.webElement.getText();
  }

  @Override
  public List<WebElement> findElements(By by) {
    return this.webElement.findElements(by);
  }

  @Override
  public WebElement findElement(By by) {
    return this.webElement.findElement(by);
  }

  @Override
  public void clear() {
    this.webElement.clear();
  }

  @Override
  public String getTagName() {
    return this.webElement.getTagName();
  }

  @Override
  public String getAttribute(String s) {
    return Optional.ofNullable(this.webElement.getDomProperty(s))
        .orElseGet(() -> this.webElement.getDomAttribute(s));
  }

  @Override
  public boolean isSelected() {
    return this.webElement.isSelected();
  }

  @Override
  public boolean isEnabled() {
    return this.webElement.isEnabled();
  }

  @Override
  public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
    return this.webElement.getScreenshotAs(target);
  }

  @Override
  public WebElement getWrappedWebElement() {
    return this.webElement;
  }

  @Override
  public String getSource() {
    return (String) this.js.executeScript("return arguments[0].innerHTML;", this.webElement);
  }

  @Override
  public void scrollTo() {
    this.js.executeScript("arguments[0].scrollIntoViewIfNeeded();", this.webElement);
  }

  @Override
  public boolean isInsideFrame() {
    return this.js.executeScript("return window.frameElement") != null;
  }

  @Override
  public boolean isVisibleInViewport() {
    return (Boolean)
        this.js.executeScript(
            """
                    var elem = arguments[0],
                    box = elem.getBoundingClientRect(),
                    cx = box.left + box.width / 2,
                    cy = box.top + box.height / 2,
                    e = document.elementFromPoint(cx, cy);
                    for (; e; e = e.parentElement) {
                      if (e === elem)
                        return true;
                    }
                    return false;
                    """,
            this.webElement);
  }

  public static ElementImpl of(WebElement element) {
    return new ElementImpl(element);
  }
}
