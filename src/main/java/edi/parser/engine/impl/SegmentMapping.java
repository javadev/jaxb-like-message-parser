package edi.parser.engine.impl;

import edi.parser.engine.Adapter;
import edi.parser.engine.Segment;
import edi.parser.engine.SegmentDeclarationTypeA;

import java.lang.reflect.Field;

public class SegmentMapping extends Mapping {
    private Segment segmentAnnotation;
    private SegmentDeclarationTypeA segmentDeclaration;
    private Adapter adapter;

    public SegmentMapping(Segment segmentDate, Field propertyName, Adapter adapter) {
        super(propertyName);
        this.segmentAnnotation = segmentDate;
        this.adapter = adapter;
        order = segmentAnnotation.order();
    }

    public Segment getSegmentAnnotation() {
        return segmentAnnotation;
    }

    public void setSegmentAnnotation(Segment segmentAnnotation) {
        this.segmentAnnotation = segmentAnnotation;
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public String getSegmentName() {
        return segmentDeclaration.name();
    }

    public boolean isRequred() {
        return segmentAnnotation.required();
    }

    public int getCount() {
        return segmentAnnotation.count();
    }


    public boolean isUnsortSeparator() {
        return segmentAnnotation.unsortSeparator();
    }

}
