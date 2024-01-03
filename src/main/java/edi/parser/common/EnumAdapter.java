package edi.parser.common;

import edi.parser.common.impl.EnumAdapterImpl;
import edi.parser.engine.BindAnnotation;

import java.lang.annotation.*;

@BindAnnotation(adapter = EnumAdapterImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface EnumAdapter {
    String regexp() default "\\w+";
}
