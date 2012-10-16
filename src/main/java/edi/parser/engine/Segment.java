package edi.parser.engine;

import edi.parser.message.common.parser.AnnotationSegmentAdapter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Any field of message which represent segemnt have to be annotated with this notation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Documented
public @interface Segment {
    /**
     * Used for sorting parsed fields, usefull for
     * creating abstract classes with header and trailer.
     *
     * @return
     */
    int order() default 0;

    /**
     * if requred == false this is max count else mondatory for count>1 segment
     * must be list
     */
    int count() default 1;

    /**
     * Is segment can be null
     *
     * @return
     */
    boolean required() default false;

    /**
     * Not used, use count==1 instead
     *
     * @return
     */
    boolean listAdapter() default false;

    /**
     * Specific adapter for segment.
     * AnnotationSegmentAdapter default adapter with use annotations on segment to parse it.
     * @return
     */
    Class< ? extends Adapter> adapter() default AnnotationSegmentAdapter.class;

    /**
     * Separator wich define groups of segments that can follow in message in any order - means unsorted group.
     * Have to be at least two in on message (open and close separator)
     * @return
     */
    boolean unsortSeparator() default false;

}
