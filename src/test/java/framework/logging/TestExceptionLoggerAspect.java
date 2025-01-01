package framework.logging;

import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class TestExceptionLoggerAspect {

  private static final Logger logger = LoggerFactory.getLogger(TestExceptionLoggerAspect.class);

  @Pointcut(
      "execution(@org.junit.jupiter.api.Test * *(..)) "
          + "|| execution(@org.junit.jupiter.params.ParameterizedTest * *(..)) "
          + "|| execution(@org.junit.jupiter.api.TestTemplate * *(..))")
  public void testMethod() {}

  @Pointcut("execution(* org.assertj.core.api.SoftAssertionsProvider.assertAll(..))")
  public void softAssertionsProvider() {}

  @Pointcut("testMethod() || softAssertionsProvider()")
  public void combined() {}

  @AfterThrowing(pointcut = "combined()", throwing = "throwable")
  public void logException(JoinPoint jp, Throwable throwable) {
    logger.error(this.convertToString(this.getMethod(jp)) + " caused " + throwable);
  }

  private Method getMethod(JoinPoint jp) {
    return ((MethodSignature) jp.getSignature()).getMethod();
  }

  private String convertToString(Method method) {
    return method.getDeclaringClass().getCanonicalName() + "." + method.getName();
  }
}
