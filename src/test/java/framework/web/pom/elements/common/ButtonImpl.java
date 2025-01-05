package framework.web.pom.elements.common;

import framework.web.pom.elements.Button;
import java.util.Objects;
import org.openqa.selenium.WebElement;

@SuppressWarnings("NullableProblems")
public class ButtonImpl extends ElementImpl implements Button {

  public ButtonImpl(WebElement webElement) {
    super(webElement);
  }

  @Override
  public String getText() {
    return Objects.requireNonNull(super.getAttribute("title"));
  }

  public static ButtonImpl of(WebElement webElement) {
    return new ButtonImpl(webElement);
  }
}
