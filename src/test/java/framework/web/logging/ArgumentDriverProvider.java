package framework.web.logging;

import org.junit.jupiter.api.extension.ReflectiveInvocationContext;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

public class ArgumentDriverProvider {

    private final ReflectiveInvocationContext<Method> invocationContext;

    ArgumentDriverProvider(ReflectiveInvocationContext<Method> invocationContext) {
        this.invocationContext = invocationContext;
    }

    public List<WebDriver> provide() {
        return this.invocationContext.getArguments()
                        .stream()
                        .filter(a -> WebDriver.class.isAssignableFrom(a.getClass()))
                        .map(WebDriver.class::cast)
                        .collect(Collectors.toList());
    }
}
