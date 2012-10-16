package edi.parser.engine;

public class ParseException extends RuntimeException {

    private final AdditionalData data = new AdditionalData();

    /**
     *
     */
    private class AdditionalData {
        private SegmentReader segmentReader;
        private Object messageObject;
    }

    public ParseException() {
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }


    public SegmentReader getSegmentReader() {
        return data.segmentReader;
    }

    public void setSegmentReader(SegmentReader segmentReader) {
        this.data.segmentReader = segmentReader;
    }

    public Object getMessageObject() {
        return data.messageObject;
    }

    public void setMessageObject(Object messageObject) {
        this.data.messageObject = messageObject;
    }
}
