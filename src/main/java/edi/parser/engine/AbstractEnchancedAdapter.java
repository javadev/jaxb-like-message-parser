package edi.parser.engine;

public abstract class AbstractEnchancedAdapter<T> implements EnchancedAdapter<T> {

    public T deserialize(SegmentReader segmentReader) throws ParseException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public T deserialize(String data) throws ParseException {
        throw new UnsupportedOperationException("Not implemented");
    }

    public String serialize(T object) throws ParseException {
        throw new UnsupportedOperationException("Not implemented");
    }

}
