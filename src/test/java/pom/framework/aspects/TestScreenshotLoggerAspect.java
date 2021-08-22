package pom.framework.aspects;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

//should we care about threadLocal<driver>?

@Aspect
public class TestScreenshotLoggerAspect {

    @Pointcut("execution(@org.junit.jupiter.api.Test * *(..)) " +
            "|| execution(@org.junit.jupiter.params.ParameterizedTest * *(..)) " +
            "|| execution(@org.junit.jupiter.api.TestTemplate * *(..))")
    public void scope() {}

    @AfterThrowing(value = "scope()", throwing = "throwable")
    public void attachScreenshot(JoinPoint jp, Throwable throwable) {
        Method method = getMethod(jp);
        Object[] args = jp.getArgs();
        Field[] fields = FieldUtils.getAllFields(jp.getThis().getClass());

        List<WebDriver> drivers = getDriver(args, fields, jp);

        if (drivers.size() > 0)
            drivers.forEach(driver -> {
                try {
                    if (Objects.nonNull(driver))
                        doAttachScreenshotToReport(method.getName(), driver);
                } catch (IOException ignored) {
                    //do nothing
                }
            });
    }

    private List<WebDriver> getDriver(Object[] args, Field[] fields, JoinPoint jp) {
        return Stream.concat(doGetDriver(args).stream(), doGetDriver(fields, jp).stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<WebDriver> doGetDriver(Object[] args) {
        if (Objects.isNull(args))
            return Collections.emptyList();

        return Arrays.stream(args)
                .filter(obj -> WebDriver.class.isAssignableFrom(obj.getClass()))
                .map(obj -> (WebDriver) obj)
                .collect(Collectors.toList());
    }

    private List<WebDriver> doGetDriver(Field[] fields, JoinPoint jp) {
        if (Objects.isNull(fields))
            return Collections.emptyList();

        return Arrays.stream(fields)
                .filter(isOfTypeWebDriver(jp))
                .map(mapToWebDriver(jp))
                .collect(Collectors.toList());
    }

    private void doAttachScreenshotToReport(String name, WebDriver driver) throws IOException {
        try (InputStream is = new ByteArrayInputStream(takeScreenshot(driver))) {
            Allure.addAttachment(name, is);
        }
    }

    private byte[] takeScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }

    private Method getMethod(JoinPoint pjp) {
        return ((MethodSignature) pjp.getSignature()).getMethod();
    }

    private Predicate<Field> isOfTypeWebDriver(JoinPoint jp) {
        return field -> {
            try {
                field.setAccessible(true); //in case driver is private
                return WebDriver.class.isAssignableFrom(field.get(jp.getThis()).getClass());
            } catch (IllegalAccessException e) {
                return false;
            }
        };
    }

    private Function<? super Field, ? extends WebDriver> mapToWebDriver(JoinPoint jp) {
        return field -> {
            try {
                field.setAccessible(true);
                return (WebDriver) field.get(jp.getThis());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getCause());
            }
        };
    }
}
