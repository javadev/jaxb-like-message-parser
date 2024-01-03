package edi.parser.engine;

public interface SegmentReader {

    /**
     * Read from current position and move to next segment.
     * Used for segment-like grammars like BSP and edifact.
     * If you use plain text grammar try use readUnparsedData and setReadedCharsCount instead
     * NOT RECOMEDED in plain text grammar:
     * If we use setReadedCharsCount we not move to next segment and return to cuurent.
     *
     * @return
     */
    String readSegment();

    /**
     * We must use setPosition/getProsition insetead
     *
     * @return
     */
    @Deprecated
    SegmentReader clone();

    String readUnparsedData();

    /**
     * Move cursor by charCount.
     *
     * @param charCount
     * @return true if the segment string is end, false otherwise
     */
    boolean setReadedCharsCount(int charCount);

    boolean isFinished();

    int getProsition();

    void setPosition(int position);

    Object getContainer();

    void setContainer(Object value);

    String getSeparator();
}
