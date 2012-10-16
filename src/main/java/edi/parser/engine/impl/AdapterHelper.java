package edi.parser.engine.impl;

import edi.parser.engine.Adapter;
import edi.parser.engine.CustomizableAdapter;

import java.lang.reflect.Field;

public class AdapterHelper {
    public static <T> T create(Class<T> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't create class", e);
        }
    }

    public static Adapter create(Class< ? extends Adapter> aClass, Field field) {
        try {
            Adapter result = create(aClass);
            if (result instanceof CustomizableAdapter) {
                ((CustomizableAdapter) result).setField(field);
            }
            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
