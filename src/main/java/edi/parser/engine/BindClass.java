package edi.parser.engine;

import edi.parser.message.adapter.StringAdapter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface BindClass {
    Class<? extends Adapter> adapter() default StringAdapter.class;
}
