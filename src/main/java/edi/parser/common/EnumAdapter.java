package edi.parser.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import edi.parser.common.impl.EnumAdapterImpl;
import edi.parser.engine.BindAnnotation;

@BindAnnotation(adapter = EnumAdapterImpl.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface EnumAdapter {
    String regexp() default "\\w+";
}
