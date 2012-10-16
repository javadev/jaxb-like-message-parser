package edi.parser.engine.impl;

import edi.parser.engine.SegmentReader;
import org.apache.log4j.Logger;

/**
 * This class contains clear implementation of all (expect depracted methods)
 * and becouse of that have to be used evrywhere.
 */
public class SegmentReaderOneStringImpl implements SegmentReader, Cloneable {
    private static final Logger LOG = Logger.getLogger(SegmentReaderOneStringImpl.class);

    private String data;

    private String separator;

    private int currentPosition;

    private int preveriosPosition = -1;

    private Object container;

    public SegmentReaderOneStringImpl(String data, String separator) {
        super();
        this.data = data;
        this.separator = separator;
    }

    @Override
    public SegmentReader clone() {
        try {
            return (SegmentReaderOneStringImpl) super.clone();
        } catch (Exception e) {
            return null;
        }
    }

    public Object getContainer() {
        return container;
    }

    public int getProsition() {
        return Math.min(currentPosition, data.length());
    }

    public boolean isFinished() {
        return currentPosition == data.length();
    }

    public String readSegment() {
        String leftString = readUnparsedData(); //MUC1A...
        int sub = getEndOfSegment(leftString); //12
        // currentPosition += sub + separator.length();
        if (currentPosition == preveriosPosition) {
            // move to next segment string
            currentPosition += sub + separator.length(); //14
            leftString = readUnparsedData(); //1ALPHA...
            sub = getEndOfSegment(leftString); //26
        }
        preveriosPosition = currentPosition; //14
        return leftString.substring(0, sub); //1ALPHA/A 1BETA/B 1CHARLY/C
    }

    public String readSegmentSafe() {
        int currentPosition = this.currentPosition; //0
        String leftString = readUnparsedData(); //MUC1A...
        int sub = getEndOfSegment(leftString); //12
        // currentPosition += sub + separator.length();
        if (this.currentPosition == this.preveriosPosition) {
            // move to next segment string
            this.currentPosition += sub + separator.length(); //14
            leftString = readUnparsedData(); //MUC1A...
            sub = getEndOfSegment(leftString); //12
        }
        this.currentPosition = currentPosition;
        return leftString.substring(0, sub); //MUC1A A320DV
    }

    private int getEndOfSegment(String leftString) {
        int sub = leftString.indexOf(separator);
        if (sub == -1) {
            // end of stream
            return leftString.length();
            // throw new ParseException("No separator found string=
            // "+leftString);
        }
        return sub;
    }

    public String readUnparsedData() {
        return data.substring(getProsition());
    }

    public void setContainer(Object value) {
        container = value;
    }

    public void setPosition(int position) {
        currentPosition = position;
        preveriosPosition = -1;
    }

    public boolean setReadedCharsCount(int charCount) {
        // currentPosition = preveriosPosition;
        currentPosition += charCount;
        String leftString = readUnparsedData();
        if (leftString.startsWith(separator)) {
            currentPosition += separator.length();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        int begin = Math.max(0, getProsition() - 300);
        int end = Math.min(data.length(), getProsition() + 300);

        return "SegmentReader[position=" + getProsition() + ", string={\n" + data.substring(begin, getProsition()) + "#CURSOR#" + data.substring(getProsition(), end) + "\n}]";
    }

    public String getSeparator() {
        return separator;
    }
}
