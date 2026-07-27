package twilightforest.beanification;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostConstruct {
    Bus value() default Bus.MOD;

    enum Bus {
        MOD,
        GAME
    }
}
