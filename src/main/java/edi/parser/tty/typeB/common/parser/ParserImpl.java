package edi.parser.tty.typeB.common.parser;


import edi.parser.engine.Adapter;
import edi.parser.engine.ParseException;
import edi.parser.engine.SegmentReader;
import edi.parser.engine.impl.AbstractParser;
import edi.parser.engine.impl.SegmentReaderImpl;
import edi.parser.tty.typeB.common.Parser;

import java.util.HashMap;
import java.util.Map;

public class ParserImpl implements Parser {
    private Map<String, Adapter> adapters = new HashMap<String, Adapter>();


    public Map<String, Adapter> getAdapters() {
        return adapters;
    }

    public Object deserialize(String data) {
        AbstractParser adapter = (AbstractParser) adapters.get(getMessageIdentifier(data));
        if (adapter == null) {
            throw new ParseException("Not registered message identifier");
        }
        SegmentReader segmentReader = new SegmentReaderImpl(data, "\r\n");
        segmentReader.readSegment();
        return adapter.deserialize(segmentReader);
    }

    public String getMessageIdentifier(String data) {
        return new SegmentReaderImpl(data, "\r\n").readSegment();
    }
}
