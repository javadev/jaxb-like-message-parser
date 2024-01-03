package edi.parser.engine.impl;

import edi.parser.engine.*;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * Message Parser for typeB messages, in future can replace MessageParser in message
 */
public class MessageParser implements AbstractParser, ThreadSafeAdapter {
    private static final Logger LOG = Logger.getLogger(MessageParser.class);

    private List<Mapping> mappings;
    protected Class messageClass;
    private String separator;
    protected int lookahed = 1;
    protected boolean useLookahedWithAllRequired = true;
    private Constructor<? extends SegmentReader> segmentReaderConstructor;


    public void setLookahed(int lookahed) {
        this.lookahed = lookahed;
    }

    public void setMappings(List<Mapping> mappings) {
        this.mappings = mappings;
    }


    public List<Mapping> getMappings() {
        return mappings;
    }

    public void setSegmentReaderClass(Class<? extends SegmentReader> segmentReaderClass) {
        try {
            segmentReaderConstructor = segmentReaderClass.getConstructor(String.class, String.class);
        } catch (Exception e) {
            throw new ParserBuildException("No constructor with String,String for " + segmentReaderClass);
        }
    }

    public MessageParser(Class messageClass, String separator) {
        this.messageClass = messageClass;
        this.separator = separator;
    }

    public String serialize(Object object) {
        if (object == null) {
            return "";
        }
        LOG.debug("Serialize class = " + object.getClass());
        StringBuffer stringBuffer = new StringBuffer();
        for (Mapping mapping : mappings) {
            Object value = mapping.getValue(object);
            Adapter adapter = getAdapter(mapping);
            SeparatorImpl separator = mapping.getSeparator();
            if (value != null) {
                LOG.debug(String.format("Use adapter[%s] for serialize object class[%s]", adapter, value != null ? value.getClass() : null));
                if (value instanceof List && mapping.getCount() > 1) {
                    stringBuffer.append(separator.getBefore());
                    List list = (List) value;
                    for (int i = 0; i < list.size(); ++i) {
                        adapter = getAdapter(mapping);
                        Object element = list.get(i);
                        if (i > 0) {
                            stringBuffer.append(separator.getBetween());
                        }
                        stringBuffer.append(adapter.serialize(element));
                    }
                    stringBuffer.append(separator.getAfter());
                } else {
                    String serializedValue = adapter.serialize(value);
                    if (serializedValue != null && serializedValue.length() > 0) {
                        stringBuffer.append(separator.getBefore()).append(serializedValue).append(separator.getAfter());
                    }
                }
            }
        }
        return stringBuffer.toString();

    }

    public Object deserialize(String data) {
        SegmentReader segmentReader;
        try {
            segmentReader = segmentReaderConstructor.newInstance(data, separator);
        } catch (Exception e) {
            throw new ParseException("Can't create segmentReader " + segmentReaderConstructor.getDeclaringClass());
        }
        return deserialize(segmentReader);
    }


    public Object deserialize(SegmentReader segmentReader) {
        Object message = null;
        try {

            Object prevConatiner = segmentReader.getContainer();
            try {
                message = messageClass.newInstance();
            } catch (Exception e) {
                throw new ParseException("Can't instantiate message, class=" + messageClass, e);
            }
            //LOG.debug("Parsing class=" + messageClass);
            int separatorStart = Integer.MAX_VALUE;
            int separatorEnd = Integer.MIN_VALUE;
            int lastParsedMapping = 0;
            //read four message
            for (int i = 0; i < mappings.size(); ++i) {
                Mapping mapping = mappings.get(i);
                Mapping saveMapping = mapping;

                Adapter adapter = getAdapter(mapping);
                Object value = deserialize(adapter, segmentReader);

                if ((useLookahedWithAllRequired ? i == lookahed - 1 : i <= lookahed - 1) && value == null) {
                    // not accept string data
                    return null;
                } else {
                    // parsing is ok, only exception will be thrown
                    LOG.debug("Parsing class=" + messageClass);
                }
                //set after parsing start
                segmentReader.setContainer(message);
                //open unsorted group
                if (mapping.isUnsortSeparator() && separatorStart == Integer.MAX_VALUE) {
                    separatorStart = i;
                    //set end
                    for (separatorEnd = separatorStart + 1; separatorEnd < mappings.size(); separatorEnd++) {
                        if (mappings.get(separatorEnd).isUnsortSeparator()) {
                            break;
                        }
                    }

                }

                //try to use unsort group parsing
                if (value == null && separatorStart != Integer.MAX_VALUE) {
                    LOG.warn("Try to use unsorted group");
                    for (int j = separatorStart; j <= separatorEnd; j++) {
                        mapping = mappings.get(j);
                        adapter = getAdapter(mapping);
                        value = deserialize(adapter, segmentReader);
                        if (value != null) {
                            break;
                        }
                    }
                }

                if (mapping.getCount() == 1) {
                    if (value != null) {
                        mapping.setValue(message, value);
                        lastParsedMapping = i;
                    }
                    if (value == null && mapping.isRequred()) {
                        throw new ParseException(String.format("No required field %s, %s. %s", mapping.toString(), segmentReader.toString(), genereteExpectedMapping(lastParsedMapping)));
                    }
                } else if (mapping.getCount() > 1) {
                    if (value == null) {
                        if (mapping.isRequred()) {
                            throw new ParseException(String.format("No required list %s, %s. %s", mapping.toString(), segmentReader.toString(), genereteExpectedMapping(lastParsedMapping)));
                        } else {
                            //lookahead fail
                            continue;
                        }
                    }
                    do {
                        mapping.setValue(message, value);
                        adapter = getAdapter(mapping);
                        value = deserialize(adapter, segmentReader);
                        lastParsedMapping = i;
                    } while (value != null);
                } else {
                    throw new ParseException("Mapping count < 0, count = " + mapping.getCount());
                }

                //close unsorted group
                if (saveMapping.isUnsortSeparator() && separatorStart != Integer.MAX_VALUE && separatorStart != i) {
                    separatorStart = Integer.MAX_VALUE;
                }

            }
            segmentReader.setContainer(prevConatiner);
            return message;

        } catch (ParseException e) {
            e.setMessageObject(message);
            e.setSegmentReader(segmentReader);
            throw e;
        }
    }

    private String genereteExpectedMapping(int currentMapping) {
        StringBuffer sb = new StringBuffer("Expected mapping on this posistion:\n");
        for (int i = currentMapping + 1; i < mappings.size(); ++i) {
            sb.append(mappings.get(i).toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    private Object deserialize(Adapter adapter, SegmentReader segmentReader) {
        Object result;
        int savedPosition = segmentReader.getProsition();
        //LOG.debug("Start using adapter=" + adapter.getClass().getSimpleName());
        if (segmentReader.isFinished()) {
            result = null;
        } else if (adapter instanceof EnchancedAdapter) {
            result = ((EnchancedAdapter) adapter).deserialize(segmentReader);
        } else {
            String data = segmentReader.readSegment();
            result = adapter.deserialize(data);
        }

        if (result == null && !(adapter instanceof PlainTextAdapter)) {
            segmentReader.setPosition(savedPosition);
        }
        LOG.debug("Adapter=" + adapter + ", used=" + (result != null));

        return result;
    }

    protected Adapter getAdapter(Mapping mapping) {
        try {
            Adapter adapter = mapping.getAdapter();
            if (adapter instanceof ThreadSafeAdapter) {
                return adapter;
            } else {
                return mapping.getAdapter().getClass().newInstance();
            }
        } catch (Exception e) {
            throw new ParserBuildException(e);
        }
    }

    public String toString() {
        return String.format("%s[class=%s]", getClass().getSimpleName(), messageClass.getSimpleName());
    }

}
