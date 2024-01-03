package edi.parser.message.adapter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface IntegerFormat {
    String format() default "";
}
