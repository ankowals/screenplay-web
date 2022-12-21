package tracing;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v105.log.model.LogEntry;
import org.openqa.selenium.devtools.v107.log.Log;
import org.openqa.selenium.devtools.v107.network.Network;
import org.openqa.selenium.devtools.v107.network.model.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Aspect
public class TracingAspect {

    //should we care about threadLocal<driver>?

    private static final Logger log = LoggerFactory.getLogger(TracingAspect.class);

    @Pointcut("execution(@org.junit.jupiter.api.Test * *(..)) " +
            "|| execution(@org.junit.jupiter.params.ParameterizedTest * *(..)) " +
            "|| execution(@org.junit.jupiter.api.TestTemplate * *(..))")
    public void testMethod() {}

    @Around("testMethod()")
    public Object trace(ProceedingJoinPoint pjp) throws Throwable {
        getDriver(pjp).forEach(driver -> {
                    HasDevTools devToolsDriver = (HasDevTools) driver;
                    DevTools devTools = devToolsDriver.getDevTools();
                    devTools.createSessionIfThereIsNotOne();
                    devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
                    devTools.send(Log.enable());

                    registerNetworkRequestListener(devTools);
                    registerNetworkResponseListener(devTools);
                    registerLogListener(devTools);
                    logJavaScriptExceptions(devTools);
                });

        return pjp.proceed();
    }

    private List<WebDriver> getDriver(ProceedingJoinPoint pjp) {
        return Arrays.stream(FieldUtils.getAllFields(pjp.getThis().getClass()))
                .filter(Objects::nonNull)
                .filter(isOfTypeWebDriver(pjp))
                .map(mapToWebDriver(pjp))
                .filter(driver -> driver.getClass().isAssignableFrom(ChromiumDriver.class) ||
                        driver.getClass().isAssignableFrom(EdgeDriver.class)) //CDP not supported by Firefox driver and others
                .collect(Collectors.toList());
    }

    private Predicate<Field> isOfTypeWebDriver(ProceedingJoinPoint pjp) {
        return field -> {
            try {
                field.setAccessible(true); //in case driver is private
                if (field.get(pjp.getThis()) != null) //in case driver is not set
                    return WebDriver.class.isAssignableFrom(field.get(pjp.getThis()).getClass());

                return false;
            } catch (IllegalAccessException e) {
                return false;
            }
        };
    }

    private Function<? super Field, ? extends WebDriver> mapToWebDriver(ProceedingJoinPoint pjp) {
        return field -> {
            try {
                field.setAccessible(true);
                return (WebDriver) field.get(pjp.getThis());
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getCause());
            }
        };
    }

    private void logJavaScriptExceptions(DevTools devTools) {
        devTools.getDomains()
                .events()
                .addJavascriptExceptionListener(e -> {
                    log.error("Java script exception occurred : {}", e.getMessage());
                    e.printStackTrace();
                });
    }

    private void registerNetworkRequestListener(DevTools devTools) {
        devTools.addListener(Network.requestWillBeSent(), entry -> {
            Request request = entry.getRequest();
            if (entry.getType().equals(Optional.of(ResourceType.FETCH))) {
                if (request.getPostData().isPresent()) {
                    log.info("[{}] Request with URL : {} : With body : {}",
                            request.getMethod(),
                            request.getUrl(),
                            request.getPostData().get());
                } else {
                    log.info("[{}] Request with URL : {}",
                            request.getMethod(),
                            request.getUrl());
                }
            }
        });
    }

    private void registerNetworkResponseListener(DevTools devTools) {
        devTools.addListener(Network.responseReceived(), entry -> {
            Response response = entry.getResponse();
            if (entry.getType().equals(ResourceType.FETCH) || entry.getType().equals(ResourceType.XHR)) {
                if (response.getStatus() >= 400) {
                    log.error("Response with URL : {} : With status code : {}",
                            response.getUrl(),
                            response.getStatus());
                } else {
                    log.info("Response with URL : {} : With status code : {}",
                            response.getUrl(),
                            response.getStatus());
                }
            }
        });
    }

    private void registerLogListener(DevTools devTools) {
        devTools.addListener(Log.entryAdded(), entry -> {
            if (entry.getLevel().equals(LogEntry.Level.ERROR)) {
                log.error("[LOG.ERROR] Entry added with text: {}", entry.getText());
                if (entry.getStackTrace().isPresent()) {
                    log.error("[LOG.ERROR]\tWith stack trace : {}", entry.getStackTrace().get());
                }
            }
        });
    }
}
