package edi.parser.message.common;

import java.lang.annotation.*;

/**
 * Annotation for composite element in segment
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface SegmentCompositeElement {

    /**
     * Maximum count of CompositeElement in segment
     *
     * @return
     */
    int count() default 1;

    /**
     * Can composite element be null or not
     *
     * @return
     */
    boolean required() default false;

    String code();
}
