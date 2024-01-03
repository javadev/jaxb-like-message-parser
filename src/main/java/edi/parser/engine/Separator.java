package edi.parser.engine;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Separator {
    String before() default "";

    String after() default "";

    String between() default "";
}
