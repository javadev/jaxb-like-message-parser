package edi.parser.engine.impl;

import edi.parser.engine.Adapter;
import edi.parser.engine.EnchancedAdapter;
import edi.parser.engine.SegmentReader;

public class CompositeAdapter implements EnchancedAdapter {

    private Adapter[] adapters;


    public void setAdapters(Adapter[] adapters) {
        this.adapters = adapters;
    }

    public Object deserialize(SegmentReader segmentReader) {
        return null;
    }

    public String serialize(Object object) {
        return null;
    }

    public Object deserialize(String data) {
        return null;
    }
}
