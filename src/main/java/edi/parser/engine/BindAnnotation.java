package edi.parser.engine;

import edi.parser.message.adapter.StringAdapter;

import java.lang.annotation.*;

/**
 * Bind annotation with specefic parser
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Documented
@Segment(adapter = StringAdapter.class)
public @interface BindAnnotation {
    Class<? extends Adapter> adapter() default StringAdapter.class;
}
