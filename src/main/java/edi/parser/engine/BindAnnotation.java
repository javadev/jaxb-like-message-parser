package edi.parser.engine;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import edi.parser.message.adapter.StringAdapter;

/**
 * Bind annotation with specefic parser
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
@Documented
@Segment(adapter = StringAdapter.class)
public @interface BindAnnotation {
    Class< ? extends Adapter> adapter() default StringAdapter.class;
}
