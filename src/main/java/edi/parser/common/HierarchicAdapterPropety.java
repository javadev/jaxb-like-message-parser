package edi.parser.common;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import edi.parser.engine.Adapter;

@Retention(RetentionPolicy.RUNTIME)
public @interface HierarchicAdapterPropety {
    Class< ? extends Adapter> adapter();
    Class objectClass();
}
