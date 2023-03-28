package framework.logging;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Stream;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

@Aspect
public abstract class AbstractLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLogAspect.class);

    @Pointcut
    abstract void scope();

    @Around("scope()")
    public Object logMethodExecution(ProceedingJoinPoint pjp) throws Throwable {
        Method method = getMethod(pjp);
        Object[] args = pjp.getArgs();

        if (Modifier.isPublic(method.getModifiers()))
            logger.debug("running: " + getMethodCall(method, args));
        else
            logger.trace("running: " + getMethodCall(method, args));

        return pjp.proceed();
    }

    private Method getMethod(ProceedingJoinPoint pjp) {
        return ((MethodSignature) pjp.getSignature()).getMethod();
    }

    private String getMethodCall(Method method, Object[] args) {
        return method.getDeclaringClass().getCanonicalName() + "." + method.getName() + "(" + convertArgs(args) + ")";
    }

    private String convertArgs(Object[] args) {
        StringBuilder params = new StringBuilder();
        for (Object arg : args) {
            if (isCollection(arg))
                params.append(",").append(asString((Collection<?>) arg));
            else if (isArray(arg))
                params.append(",").append(asString(List.of((Array) arg)));
            else if (isStream(arg))
                params.append(",").append(asString((Stream<?>) arg));
            else if (isBaseType(arg))
                params.append(",").append(arg);
            else
               params.append(",").append(toStringWithAttributes(arg, SHORT_PREFIX_STYLE));
        }

        return params.toString().replaceFirst(",", "");
    }

    private String asString(Collection<?> collection) {
        StringBuilder b = new StringBuilder().append(System.lineSeparator());
        collection.forEach(e -> b.append(toStringWithAttributes(e, SHORT_PREFIX_STYLE)).append(System.lineSeparator()));
        return b.toString();
    }

    private String asString(Stream<?> stream) {
        StringBuilder b = new StringBuilder().append(System.lineSeparator());
        stream.forEach(e -> b.append(toStringWithAttributes(e, SHORT_PREFIX_STYLE)).append(System.lineSeparator()));
        return b.toString();
    }

    private String toStringWithAttributes(Object object, ToStringStyle style) {
        ReflectionToStringBuilder builder = new ReflectionToStringBuilder(object, style) {
            @Override
            protected boolean accept(Field field) {
                try {
                    return super.accept(field) && Objects.nonNull(field.get(object));
                } catch (IllegalAccessException e) {
                    return super.accept(field);
                }
            }
        };
        return builder.toString();
    }

    private boolean isCollection(Object ob) {
        return ob instanceof Collection || ob instanceof Map;
    }
    private boolean isArray(Object ob) {
        return ob instanceof Array;
    }
    private boolean isStream(Object ob) {
        return ob instanceof Stream;
    }
    private boolean isBaseType(Object ob) {
        return getBaseTypes().contains(ob.getClass());
    }

    private Set<Class<?>> getBaseTypes() {
        Set<Class<?>> set = new HashSet<>();
        set.add(Boolean.class);
        set.add(Character.class);
        set.add(Byte.class);
        set.add(Short.class);
        set.add(Integer.class);
        set.add(Long.class);
        set.add(Float.class);
        set.add(Double.class);
        set.add(Void.class);
        set.add(String.class);

        return set;
    }
}
