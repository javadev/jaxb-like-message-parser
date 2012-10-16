package edi.parser.engine.impl;

import edi.parser.engine.SegmentReader;
import edi.parser.engine.CustomizableAdapter;

import java.lang.reflect.Field;

public class GroupParser extends MessageParser implements CustomizableAdapter {

    public GroupParser(Class messageClass, String separator) {
        super(messageClass, separator);
    }

    public void setField(Field field) throws Exception {
        //TODO: build parser
    }

    public Object deserialize(SegmentReader segmentReader) {
        Object result = super.deserialize(segmentReader);
        // segmentReader.backOneSegement();
        return result;
    }
}
