package edi.parser.engine;

/**
 * Adpter with special method with can parse several segemnts.
 */
public interface EnchancedAdapter<T> extends Adapter<T> {
    /**
     * @param segmentReader
     * @return null if can\t parse segemnt, Object if parse segemnt, and thrown ParseException if segement data incorrect
     */
    T deserialize(SegmentReader segmentReader) throws ParseException;
}
