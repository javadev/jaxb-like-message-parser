package edi.parser.message.common.parser;

import edi.parser.message.common.SegmentCompositeElement;
import edi.parser.engine.Adapter;
import edi.parser.engine.impl.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;

public class CompositeElementMapping extends Mapping {
    private Class elementClass;
    private List<SegmentElementMapping> mapping = new ArrayList<SegmentElementMapping>();
    private SegmentCompositeElement segmentCompositeElementAnnotation;


    public CompositeElementMapping() {
        super();
        this.elementClass = null;
    }


    public CompositeElementMapping(Field propertyName, Class elementClass, SegmentCompositeElement segmentCompositeElementAnnotation) {
        super(propertyName);
        this.elementClass = elementClass;
        this.segmentCompositeElementAnnotation = segmentCompositeElementAnnotation;
    }


    public void setMapping(List<SegmentElementMapping> mapping) {
        this.mapping = mapping;
    }

    public boolean isRequred() {
        return segmentCompositeElementAnnotation != null && segmentCompositeElementAnnotation.required();
    }

    public Adapter getAdapter() {
        return null;
    }


    public int getCount() {
        if (segmentCompositeElementAnnotation == null) {
            return 1;
        }
        return segmentCompositeElementAnnotation.count();
    }

    public boolean isFlat() {
        return elementClass == null;
    }

    public Class getElementClass() {
        return elementClass;
    }

    public List<SegmentElementMapping> getMapping() {
        return mapping;
    }
}
