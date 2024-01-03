package edi.parser.message.adapter;

import edi.parser.engine.CustomizableAdapter;
import edi.parser.engine.EnchancedAdapter;
import edi.parser.engine.ParseException;
import edi.parser.engine.SegmentReader;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntegerAdapter implements EnchancedAdapter, CustomizableAdapter {
    private static final Logger LOG = Logger.getLogger(IntegerAdapter.class);
    private final Pattern pattern = Pattern.compile("^\\s?(\\d+)", Pattern.CASE_INSENSITIVE);
    private IntegerFormat integerFormat;

    public Object deserialize(SegmentReader segmentReader) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(String.format("deserialize SegmentReader " + segmentReader));
        }
        String data = segmentReader.readSegment();
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            segmentReader.setReadedCharsCount(matcher.group(0).length());
            return Integer.valueOf(matcher.group(1));
        }
        return null;
    }

    public void setField(Field field) {
        integerFormat = field.getAnnotation(IntegerFormat.class);
    }

    public String serialize(Object object) throws ParseException {
        if (LOG.isTraceEnabled()) {
            LOG.debug(String.format("serialize Object " + object));
        }
        if (object instanceof Integer) {
            Integer integer = (Integer) object;
            if (integerFormat != null) {
                String format = integerFormat.format();
                return String.format(format, integer);
            } else {
                return object.toString();
            }
        }
        return "";
    }

    public Integer deserialize(String data) {
        if (LOG.isTraceEnabled()) {
            LOG.debug(String.format("deserialize String " + data));
        }
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            return Integer.valueOf(matcher.group(0));
        }
        return null;
    }
}
