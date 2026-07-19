package com.github.ankowals.framework.logging;

import java.lang.reflect.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public abstract class AbstractLogAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLogAspect.class);

  @Pointcut
  abstract void scope();

  @Before("scope()")
  public void logMethodExecution(JoinPoint joinPoint) {
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

    if (Modifier.isPublic(method.getModifiers())) {
      LOGGER.info(
          "running: {}.{}()", method.getDeclaringClass().getCanonicalName(), method.getName());
    }
  }
}
