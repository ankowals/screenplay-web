package screenplay;

import framework.web.pom.page.BasePage;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import pom.automationpractice.models.AutomationPracticeHomePage;
import pom.formio.AngularFormExamplePage;

public class Pages {
  public static final String AUTOMATION_PRACTICE_HOME = "http://automationpractice.com/index.php";
  public static final String FORM_IO_DEMO = "https://formio.github.io/angular-demo";

  private static final Map<String, Class<? extends BasePage>> URL_MAPPING;

  static {
    URL_MAPPING = new HashMap<>();
    URL_MAPPING.put(AUTOMATION_PRACTICE_HOME, AutomationPracticeHomePage.class);
    URL_MAPPING.put(FORM_IO_DEMO, AngularFormExamplePage.class);
  }

  public static Class<? extends BasePage> getMapping(String url) {
    if (!URL_MAPPING.containsKey(url)) throw new NoSuchElementException(url);

    return URL_MAPPING.get(url);
  }
}
