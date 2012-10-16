package edi.parser.engine.impl;

import edi.parser.engine.ParseException;
import edi.parser.engine.SegmentReader;

import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;

/**
 * This class has implementation of character stream,
 * it usefull for Stream of segment.
 */
public class SegmentReaderImpl implements SegmentReader, Cloneable {
    private static final Logger LOG = Logger.getLogger(SegmentReaderImpl.class);

    protected List<String> segements;
    private Object container;
    protected int i;
    private String separator;

    public String getSeparator() {
        return separator;
    }

    public SegmentReaderImpl(String data, String separator) {
        segements = new LinkedList<String>();
        segements.addAll(Arrays.asList(data.split(separator)));
        this.separator = separator;
    }

    public String readSegment() {
        if (isFinished()) {
            return null;
        }
        String result = segements.get(i++);
        LOG.debug(String.format("String readed[%d]=%s", i, result));
        return result;
    }

    public boolean setReadedCharsCount(int charCount) {
        --i;
        String current = segements.get(i);
        current = current.substring(charCount);
        if (current.length() == 0) {
            ++i;
            return true;
        } else {
            ++i;
            segements.add(i, current);
        }
        return false;
    }

    public void backOneSegement() {
        if (i - 1 < 0) {
            throw new ParseException("Can't move back");
        }
        i--;
        LOG.debug("backOneSegement");
    }

    public boolean isFinished() {
        return i > segements.size() - 1;
    }

    public void setPosition(int position) {
        i = position;
    }

    public Object getContainer() {
        return container;
    }

    public void setContainer(Object value) {
        container = value;
    }


    public int getProsition() {
        return i;
    }
    @Override
    public SegmentReader clone() {
        SegmentReaderImpl readerImpl = null;
        try {
            readerImpl = (SegmentReaderImpl) super.clone();
        } catch (CloneNotSupportedException e) {
            LOG.warn(e.getMessage(), e);
        }
        readerImpl.segements = new LinkedList<String>();
        readerImpl.segements.addAll(this.segements);
        return readerImpl;
    }

    public String readUnparsedData() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        int begin = Math.max(0, getProsition() - 5);
        int end = Math.min(segements.size(), getProsition() + 5);
        StringBuffer sb = new StringBuffer();
        for (int j = begin; j < end; ++j) {
            sb.append(segements.get(j));
            if (j == getProsition()) {
                sb.append("#CURSOR#");
            }
            sb.append(separator);
        }
        return "SegmentReader[position=" + getProsition() + ", string={\n" + sb.toString() + "\n}]";

    }


}
