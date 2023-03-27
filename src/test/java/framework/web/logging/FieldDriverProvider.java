package framework.web.logging;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstances;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.platform.commons.util.ReflectionUtils.readFieldValues;

public class FieldDriverProvider {

    private final ExtensionContext extensionContext;

    FieldDriverProvider(ExtensionContext extensionContext) {
        this.extensionContext = extensionContext;
    }

    public List<WebDriver> provide() {
        return getFromClassFields(this.extensionContext.getRequiredTestInstances());
    }

    private List<WebDriver> getFromClassFields(TestInstances testInstances) {
        Object instance = testInstances.getInnermostInstance();
        List<Field> fields = Arrays.stream(FieldUtils.getAllFields(instance.getClass()))
                .collect(Collectors.toList());

        return readFieldValues(fields, instance, isOfWebDriverType(instance))
                .stream().map(WebDriver.class::cast)
                .collect(Collectors.toList());

    }

    private Predicate<Field> isOfWebDriverType(Object instance) {
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
}
