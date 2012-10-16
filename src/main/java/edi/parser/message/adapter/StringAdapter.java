package edi.parser.message.adapter;

import edi.parser.engine.Adapter;
import edi.parser.engine.ThreadSafeAdapter;

/**
 * Adapter for simple string
 */
public class StringAdapter implements Adapter, ThreadSafeAdapter {

    public String serialize(Object object) {
        try {
            return (String) object;
        } catch (ClassCastException ex) {
            throw new ClassCastException("Error while object '" + object + "' serializing.");
        }
    }

    public Object deserialize(String data) {
        return data;
    }
}
