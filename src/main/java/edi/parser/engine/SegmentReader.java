package edi.parser.engine;

public interface SegmentReader {

    /**
     * Read from current position and move to next segment.
     * Used for segment-like grammars like BSP and edifact.
     * If you use plain text grammar try use readUnparsedData and setReadedCharsCount instead
     * NOT RECOMEDED in plain text grammar:
     * If we use setReadedCharsCount we not move to next segment and return to cuurent.
     * @return
     */
    public String readSegment();

    /**
     * We must use setPosition/getProsition insetead
     * @return
     */
    @Deprecated
    public SegmentReader clone();

    public String readUnparsedData();

    /**
     * Move cursor by charCount.
     * @param charCount
     * @return true if the segment string is end, false otherwise
     */
    public boolean setReadedCharsCount(int charCount);

    public boolean isFinished();

    public int getProsition();

    public void setPosition(int position);

    public Object getContainer();
    public void setContainer(Object value);
    public String getSeparator();
}
