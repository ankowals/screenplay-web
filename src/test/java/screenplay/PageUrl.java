package screenplay;

import framework.web.pom.page.BasePage;
import pom.automationpractice.models.HomePage;
import pom.parasoft.models.ParasoftProductsPage;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class PageUrl {
    public static final String AUTOMATION_PRACTICE_HOME = "http://automationpractice.com/index.php";
    public static final String PARASOFT_PRODUCTS = "https://www.parasoft.com/products/";

    private final static Map<String, Class<? extends BasePage>> URL_MAPPING;

    static {
        URL_MAPPING = new HashMap<>();
        URL_MAPPING.put(AUTOMATION_PRACTICE_HOME, HomePage.class);
        URL_MAPPING.put(PARASOFT_PRODUCTS, ParasoftProductsPage.class);
    }

    public static Class<? extends BasePage> getMapping(String url) {
        if(!URL_MAPPING.containsKey(url))
                throw new NoSuchElementException(url);

        return URL_MAPPING.get(url);
    }
}
