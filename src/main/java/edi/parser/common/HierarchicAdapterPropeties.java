package edi.parser.common;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface HierarchicAdapterPropeties {
    HierarchicAdapterPropety[] value();
}
