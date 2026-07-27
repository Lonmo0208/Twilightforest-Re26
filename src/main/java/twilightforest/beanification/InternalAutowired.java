package twilightforest.beanification;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InternalAutowired {
    String value() default "";
}
