package pom.framework.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

@Aspect
public class TestExceptionLoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger(TestExceptionLoggerAspect.class);

    @Pointcut("execution(@org.junit.jupiter.api.Test * *(..)) " +
            "|| execution(@org.junit.jupiter.params.ParameterizedTest * *(..)) " +
            "|| execution(@org.junit.jupiter.api.TestTemplate * *(..))")
    public void scope() {}

    @AfterThrowing(pointcut = "scope()", throwing = "throwable")
    public void logException(JoinPoint jp, Throwable throwable) {
        logger.error(convertToString(getMethod(jp)) + " caused " + throwable);
    }

    private Method getMethod(JoinPoint jp) {
        return ((MethodSignature) jp.getSignature()).getMethod();
    }

    private String convertToString(Method method) {
        return method.getDeclaringClass().getSimpleName() + "." + method.getName();
    }
}
