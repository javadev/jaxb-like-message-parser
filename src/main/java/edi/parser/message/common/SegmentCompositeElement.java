package edi.parser.message.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

/**
 * Annotation for composite element in segment
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface SegmentCompositeElement {

    /**
     * Maximum count of CompositeElement in segment
     * @return
     */
    int count() default 1;

    /**
     * Can composite element be null or not
     * @return
     */
    boolean required() default false;

    String code();
}
