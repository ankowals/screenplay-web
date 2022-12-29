package framework.web.logging;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.extension.TestInstances;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.edge.EdgeDriver;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DriverProvider {

    private final TestInstances testInstances;

    DriverProvider(TestInstances testInstances) {
        this.testInstances = testInstances;
    }

    public List<WebDriver> provide() {
        Object instance = testInstances.getInnermostInstance();
        return Arrays.stream(FieldUtils.getAllFields(instance.getClass()))
                .filter(webDriverType(instance))
                .map(mapToWebDriver(instance))
                .filter(driver -> driver.getClass().isAssignableFrom(ChromiumDriver.class) ||
                        driver.getClass().isAssignableFrom(EdgeDriver.class)) //CDP not supported by Firefox, Safari etc.
                .collect(Collectors.toList());
    }

    private Predicate<Field> webDriverType(Object instance) {
        return field -> {
            try {
                field.setAccessible(true);
                if (field.get(instance) != null)
                    return WebDriver.class.isAssignableFrom(field.get(instance).getClass());

                return false;
            } catch (IllegalAccessException e) {
                return false;
            }
        };
    }

    private Function<? super Field, ? extends WebDriver> mapToWebDriver(Object instance) {
        return field -> {
            try {
                field.setAccessible(true);
                return (WebDriver) field.get(instance);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getCause());
            }
        };
    }

}
