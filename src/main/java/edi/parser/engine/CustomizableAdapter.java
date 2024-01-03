package edi.parser.engine;

import java.lang.reflect.Field;

/**
 * Parser builder use this interface to set Field which annotated with this adpater class.
 * Using this field adapter can read any Annotations on this field.
 * Becouse of low performance of reading annotations operation this
 * adapter must be ThreadSafeAdapter(constructed only once, during parser building phase)
 */
public interface CustomizableAdapter extends ThreadSafeAdapter {
    void setField(Field field) throws Exception;
}
