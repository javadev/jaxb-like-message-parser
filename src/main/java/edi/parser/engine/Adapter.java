package edi.parser.engine;

public interface Adapter<T> {
    String serialize(T object) throws ParseException;

    T deserialize(String data) throws ParseException;
    //Object deserialize(SegmentReader segmentReader);
}
