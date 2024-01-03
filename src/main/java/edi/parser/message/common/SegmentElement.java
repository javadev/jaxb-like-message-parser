package edi.parser.message.common;

import edi.parser.engine.Adapter;
import edi.parser.message.adapter.StringAdapter;

import java.lang.annotation.*;

/**
 * Annotation for segement elements
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface SegmentElement {
    /**
     * Adapter for segment element type
     *
     * @return
     */
    Class<? extends Adapter> adapter() default StringAdapter.class;

    /**
     * Separator, must be true if this segment appears in next composite elements
     *
     * @return
     */
    boolean nextGroup() default false;

    /**
     * Can element be null or not
     *
     * @return
     */
    boolean required() default false;

    /**
     * Specefic code in EDifact specefication
     *
     * @return
     */
    String code() default "";
}
