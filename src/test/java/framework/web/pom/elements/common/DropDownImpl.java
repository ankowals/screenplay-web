package framework.web.pom.elements.common;

import framework.web.pom.elements.Dropdown;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class DropDownImpl extends ElementImpl implements Dropdown {

  public DropDownImpl(WebElement webElement) {
    super(webElement);
  }

  @Override
  public void select(String value) {
    new Select(this.webElement).selectByVisibleText(value);
  }

  @Override
  public List<String> getOptions() {
    return new Select(this.webElement).getOptions().stream().map(WebElement::getText).toList();
  }

  public static DropDownImpl of(WebElement webElement) {
    return new DropDownImpl(webElement);
  }
}
