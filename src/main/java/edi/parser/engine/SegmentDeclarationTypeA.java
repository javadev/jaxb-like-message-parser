package edi.parser.engine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

/**
 * Annotation for segment declaration
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SegmentDeclarationTypeA {

    /**
     * Segement name, for example TKT, CPN, MON.
     * @return
     */
    String name();
}
