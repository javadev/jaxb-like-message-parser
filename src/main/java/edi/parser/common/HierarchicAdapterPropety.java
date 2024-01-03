package edi.parser.common;

import edi.parser.engine.Adapter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface HierarchicAdapterPropety {
    Class<? extends Adapter> adapter();

    Class objectClass();
}
