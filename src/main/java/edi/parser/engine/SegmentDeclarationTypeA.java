package edi.parser.engine;

import java.lang.annotation.*;

/**
 * Annotation for segment declaration
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface SegmentDeclarationTypeA {

    /**
     * Segement name, for example TKT, CPN, MON.
     *
     * @return
     */
    String name();
}
