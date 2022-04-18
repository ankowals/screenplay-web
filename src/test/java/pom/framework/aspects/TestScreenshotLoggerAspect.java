package pom.framework.aspects;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
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
    public void testMethod() {}

    @Pointcut("execution(* org.assertj.core.api.SoftAssertionsProvider.assertAll(..))")
    public void softAssertionsProvider() {}

    @Pointcut("testMethod() || softAssertionsProvider()")
    public void combined() {}

    @AfterThrowing(pointcut = "combined()", throwing = "throwable")
    public void attachScreenshot(JoinPoint jp, Throwable throwable) {
        doAttachScreenshot(jp);
    }

    private List<WebDriver> getDriver(Object[] args, Field[] fields, JoinPoint jp) {
        return Stream.concat(doGetDriver(args).stream(), doGetDriver(fields, jp).stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<WebDriver> doGetDriver(Object[] args) {
        if (args == null)
            return Collections.emptyList();

        return Arrays.stream(args)
                .filter(Objects::nonNull)
                .filter(arg -> WebDriver.class.isAssignableFrom(arg.getClass()))
                .map(arg -> (WebDriver) arg)
                .collect(Collectors.toList());
    }

    private List<WebDriver> doGetDriver(Field[] fields, JoinPoint jp) {
        if (fields == null)
            return Collections.emptyList();

        return Arrays.stream(fields)
                .filter(Objects::nonNull)
                .filter(isOfTypeWebDriver(jp))
                .map(mapToWebDriver(jp))
                .collect(Collectors.toList());
    }

    private void doAttachScreenshot(JoinPoint jp) {
        Method method = getMethod(jp);
        Object[] args = jp.getArgs();
        Field[] fields = FieldUtils.getAllFields(jp.getThis().getClass());

        List<WebDriver> drivers = getDriver(args, fields, jp);

        if (!drivers.isEmpty())
            drivers.forEach( driver -> {
                try {
                    if (driver != null)
                        attachScreenshotToReport(method.getName(), driver);
                } catch (IOException ignored) {}
            });
    }

    private void attachScreenshotToReport(String name, WebDriver driver) throws IOException {
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
                if (field.get(jp.getThis()) != null) //in case driver is not set
                    return WebDriver.class.isAssignableFrom(field.get(jp.getThis()).getClass());

                return false;
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
