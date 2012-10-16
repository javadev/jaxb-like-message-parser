package edi.parser.engine.impl;

import edi.parser.engine.Adapter;
import edi.parser.engine.ParseException;
import edi.parser.engine.ParserBuildException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Map field to adapter
 * Also provide setter and getter method for object.
 * Remark : Remark: If we generete mapping and avoid reflection speed will increase two times (11.6/4.1)
 */
public abstract class Mapping implements Comparable {
    protected String propertyName;
    private Field field;
    private SeparatorImpl separator = new SeparatorImpl();

    protected int count = 1;
    protected boolean isRequred;
    protected boolean unsortSeparator;
    protected int order;

    protected Mapping(Field field) {
        if (field == null) {
            throw new ParserBuildException("Can't create mapping with null field");
        }
        field.setAccessible(true);
        this.field = field;
        propertyName = field.getName();
    }

    @Deprecated
    protected Mapping() {
    }

  /*  protected Mapping(String propertyName) {
        this.propertyName = propertyName;
    } */

    public void setValue(Object container, Object data) {


        try {
            if (getCount() == 1) {
                set(container, propertyName, data);
                //PropertyUtils.setProperty(container, propertyName, data);
            } else {
                List list = (List) get(container, propertyName);
                if (list == null) {
                    list = new ArrayList();
                    set(container, propertyName, list);
                    //PropertyUtils.setProperty(container, propertyName, list);
                }
                list.add(data);
            }
        } catch (Exception e) {
            throw new ParseException("Can't set property, name=" + propertyName + ", class=" + container.getClass());
        }
    }

    public Object getValue(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return get(object, propertyName);
        } catch (Exception e) {
            throw new ParseException("Can't get property, name=" + propertyName + ", class=" + object.getClass().getName());
        }
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public boolean isRequred() {
        return isRequred;
    }

    public abstract Adapter getAdapter();

    public int getCount() {
        return count;
    }

    public boolean isUnsortSeparator() {
        return unsortSeparator;
    }


    private Field getField(Class cls, String propertyName) throws NoSuchFieldException {
        Class clazz = cls;
        while (field == null && !clazz.equals(Object.class)) {
            try {
                field = clazz.getDeclaredField(propertyName);
                field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return field;
    }

    private void set(Object container, String propertyName, Object value) throws Exception {
        field.set(container, value);
    }

    /**
     * we can get field using   getField(container.getClass(), propertyName);
     */
    private Object get(Object container, String propertyName) throws Exception {
        return field.get(container);
    }


    public String toString() {
        return "Mapping[" + field.getDeclaringClass().getSimpleName() + '.' + field.getName() + ", adapter=" + getAdapter().toString() + ']';
    }


    public SeparatorImpl getSeparator() {
        return separator;
    }


    public void setSeparator(SeparatorImpl separator) {
        this.separator = separator;
    }


    public int compareTo(Object o) {
        Mapping o2 = (Mapping) o;
        return this.order - o2.order;
    }
}
