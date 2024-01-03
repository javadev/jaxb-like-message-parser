package edi.parser.common;

import edi.parser.engine.Adapter;
import edi.parser.engine.CustomizableAdapter;
import edi.parser.engine.ParseException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Adapter for abstract class, when knowns all implementations of this class.
 * Use HierarchicAdapterPropeties and HierarchicAdapterPropety
 * for customize those classes and theris adapters.
 */
public class HierarchicAdapter implements Adapter<Object>, CustomizableAdapter {

    protected Map<Class, Adapter> classToAdapterMap = new HashMap<Class, Adapter>();

    public void setField(Field field) throws Exception {
        HierarchicAdapterPropeties propetiesContainer = field.getAnnotation(HierarchicAdapterPropeties.class);
        HierarchicAdapterPropety[] propeties = propetiesContainer.value();
        for (HierarchicAdapterPropety propety : propeties) {
            classToAdapterMap.put(propety.objectClass(), propety.adapter().newInstance());
        }

    }


    private Adapter getAdapter(Object object) {
        Adapter adapter = classToAdapterMap.get(object.getClass());
        try {
            adapter = adapter.getClass().newInstance();
        } catch (Exception e) {
            throw new ParseException(e);
        }
        return adapter;
    }

    public String serialize(Object object) throws ParseException {

        //Adapter adapter = classToAdapterMap.get(object.getClass());
        Adapter adapter = getAdapter(object);
        if (adapter == null) {
            throw new ParseException();
        }
        return adapter.serialize(object);
    }

    public Object deserialize(String data) throws ParseException {
        Object result = null;
        for (Adapter adapter : classToAdapterMap.values()) {
            result = adapter.deserialize(data);
            if (result != null) {
                break;
            }
        }
        return result;
    }
}
