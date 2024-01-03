package edi.parser.engine;

import java.lang.annotation.*;

/**
 * Group elements on beans must be annotated with this annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Group {
    /**
     * Oreder of field in parsed text
     *
     * @return
     */
    int order() default 0;

    /**
     * For list element of bean count mut be greater than 1
     */
    int count() default 1;

    /**
     * Is group reqired
     *
     * @return
     */
    boolean required() default false;

    int lookahead() default 1;
}
