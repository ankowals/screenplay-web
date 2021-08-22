package pom.framework.page;

import org.openqa.selenium.WebDriver;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class PageFactory {

    private final WebDriver driver;

    public PageFactory(WebDriver driver) {
        this.driver = Objects.requireNonNull(driver);
    }

    public <T extends BasePage> T onPage(Class<T> clazz) {
        try {
            return init(clazz);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private <T extends BasePage> T init(Class<T> type) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return type.getConstructor(WebDriver.class).newInstance(driver);
    }
}
