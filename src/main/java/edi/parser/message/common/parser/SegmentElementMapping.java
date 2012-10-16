package edi.parser.message.common.parser;

import edi.parser.message.common.SegmentElement;
import edi.parser.engine.Adapter;
import edi.parser.engine.impl.Mapping;

import java.lang.reflect.Field;

public class SegmentElementMapping extends Mapping {
    private SegmentElement segmentElement;
    private Adapter adapter;
    private Class mappedClass;

    public SegmentElementMapping(Field propertyName, SegmentElement segmentElement, Adapter adapter, Class mapedclass) {
        super(propertyName);
        this.segmentElement = segmentElement;
        this.adapter = adapter;
        this.mappedClass = mapedclass;
    }


    public boolean isRequred() {
        return segmentElement.required();
    }

    public Class getMappedClass() {
        return mappedClass;
    }

    public Adapter getAdapter() {
        return adapter;
    }

}
