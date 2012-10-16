package edi.parser.message.common.parser;

import edi.parser.engine.Adapter;
import edi.parser.engine.CustomizableAdapter;
import edi.parser.engine.ParseException;
import edi.parser.engine.ParserBuildException;
import edi.parser.engine.SegmentDeclarationTypeA;
import edi.parser.engine.SegmentReader;
import edi.parser.engine.ThreadSafeAdapter;
import edi.parser.util.ReflectionUtil;
import edi.parser.message.EdifactMessageConstants;
import edi.parser.message.common.SegmentCompositeElement;
import edi.parser.message.common.SegmentElement;
import edi.parser.message.segment.MessageHeaderSegment;
import edi.parser.message.segment.MessageTrailerSegment;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Annotation based parser for Segemnt type A
 */
public class AnnotationSegmentAdapter implements Adapter, ThreadSafeAdapter, CustomizableAdapter {
    private static final Logger LOG = Logger.getLogger(AnnotationSegmentAdapter.class);

    private Class< ? > cls;

    private SegmentDeclarationTypeA segmentDeclaration;
    private List<CompositeElementMapping> mappings;

    private String colonSymbol = EdifactMessageConstants.AMADEUS_COLON;
    private String plusSymbol = EdifactMessageConstants.AMADEUS_PLUS_SIGN;

    /**
     * Count of segments in message, for fill MessageTrailer
     */
    private static final ThreadLocal<Integer> SEGMENT_COUNT = new ThreadLocal<Integer>();

    ///////////////////////////////////////////////////////////////////////////
    /// BUILD CODE
    public void setClass(Class< ? > cls) {
        this.cls = cls;
    }

    public Class getCls() {
        return cls;
    }

    public void setCls(Class< ? > cls) {
        this.cls = cls;
    }

    public void setMappings(List<CompositeElementMapping> mappings) {
        this.mappings = mappings;
    }



    public void setField(Field mappedField) throws Exception {
        Class< ? > type = ReflectionUtil.typeExtractor(mappedField);
        SegmentDeclarationTypeA declarationTypeA = type.getAnnotation(SegmentDeclarationTypeA.class);
        AnnotationSegmentAdapter annotationAdapter = this;
        List<Field> fileds = ReflectionUtil.getAllFields(type);
        List<CompositeElementMapping> compositeElements = new ArrayList<CompositeElementMapping>();
        //List<SegmentElementMapping> mappings = new ArrayList<SegmentElementMapping>();
        CompositeElementMapping compositeElement = null;
        for (Field field : fileds) {
            SegmentElement elementAnnotation = field.getAnnotation(SegmentElement.class);
            SegmentCompositeElement compositeElementAnnotation = field.getAnnotation(SegmentCompositeElement.class);

            if (elementAnnotation != null) {

                //create if not exsist
                if (compositeElement == null) {
                    compositeElement = new CompositeElementMapping();
                    compositeElements.add(compositeElement);
                }

                if (elementAnnotation.nextGroup()) {
                    //compositeElements.add(new CompositeElementMapping(mappings));
                    compositeElement = new CompositeElementMapping();
                    compositeElements.add(compositeElement);
                    //mappings = new ArrayList<SegmentElementMapping>();
                }

                Adapter segmentElementAdapter = elementAnnotation.adapter().newInstance();
                compositeElement.getMapping().add(new SegmentElementMapping(
                        field,
                        elementAnnotation,
                        segmentElementAdapter,
                        field.getType()
                )
                );
            } else if (compositeElementAnnotation != null) {
                compositeElement = new CompositeElementMapping(field, ReflectionUtil.typeExtractor(field), compositeElementAnnotation);
                compositeElements.add(compositeElement);
                compositeElement.setMapping(buildMappingForCompositeElement(ReflectionUtil.typeExtractor(field)));
                compositeElement = null;
                //compositeElements.add(new CompositeElementMapping(field.getName(), typeExtractor(field), mappings, compositeElementAnnotation));
            } else {
                throw new ParserBuildException(String.format("AnnotationSegmentAdapter build: Filed[%s] of class[%s] not annoted with @SegmentElement or @SegmentCompositeElement", field.getName(), type));
            }

        }
        //compositeElements.add(new CompositeElementMapping(mappings));

        annotationAdapter.setMappings(compositeElements);
        annotationAdapter.setClass(type);
        annotationAdapter.setSegmentDeclaration(declarationTypeA);

    }

    private List<SegmentElementMapping> buildMappingForCompositeElement(Class cls) throws IllegalAccessException, InstantiationException {
        List<SegmentElementMapping> mappings = new ArrayList<SegmentElementMapping>();
        List<Field> fields = ReflectionUtil.getAllFields(cls);

        for (Field field : fields) {
            SegmentElement element = field.getAnnotation(SegmentElement.class);
            if (element != null) {

                Adapter segmentElementAdapter = element.adapter().newInstance();
                mappings.add(new SegmentElementMapping(
                        field,
                        element,
                        segmentElementAdapter,
                        field.getType()
                )
                );
            }
        }
        return mappings;
    }

    /// end of BUILD CODE
    ///////////////////////////////////////////////////////////////////////////


    public String serialize(Object object) {
        if (object == null) {
            return "";
        }

        processSegmentCount(object);


        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(getSegmentName());
        stringBuffer.append(plusSymbol);
        int skipLevel2 = 0;
        for (int i = 0; i < mappings.size(); ++i) {
            CompositeElementMapping compositeElementMapping = mappings.get(i);

            Object container;
            if (compositeElementMapping.isFlat()) {
                container = object;
            } else {
                container = compositeElementMapping.getValue(object);
            }

            //for list of composite elements
            List<Object> list;
            if (!(container instanceof List)) {
                list = new ArrayList<Object>();
                list.add(container);
                container = list;
            } else {
                list = (List<Object>) container;
            }
            for (Object elem : list) {
                skipLevel2 = 0;
                //serialize elements of composite element
                for (int j = 0; j < compositeElementMapping.getMapping().size(); ++j) {
                    SegmentElementMapping mapping = compositeElementMapping.getMapping().get(j);
                    Adapter adapter = mapping.getAdapter();


                    Object value = mapping.getValue(elem);
                    if (LOG.isTraceEnabled()) {
                        LOG.trace(String.format("Use adapter[%s] for serialize object class[%s]", adapter.getClass()
                                .getSimpleName(), value != null ? value.getClass() : null));
                    }
                    if (value != null) {
                        for (int k = 0; k < skipLevel2; ++k) {
                            stringBuffer.append(colonSymbol);
                        }
                        stringBuffer.append(adapter.serialize(value));
                        skipLevel2 = 1;
                    } else {
                        skipLevel2++;
                    }
                }
                //stringBuffer.append(":");
                //if (skipLevel2 > 0) {
                stringBuffer.append(plusSymbol);
                //}
            }


        }

        //delete + at end
        int i = stringBuffer.length() - 1;
        while (stringBuffer.charAt(i) == plusSymbol.toCharArray()[0]) {
            --i;
        }

        // only if more than segment name
        stringBuffer.setLength(i > 2 ? i + 1 : 3);

        stringBuffer.append(EdifactMessageConstants.AMADEUS_APOSTROPHE);
        // delete '+' if segment is empty
        if (stringBuffer.charAt(stringBuffer.length() - 1) == plusSymbol.toCharArray()[0]) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    private void processSegmentCount(Object object) {
        //protocol level count of segment
        if (object instanceof MessageHeaderSegment) {
            SEGMENT_COUNT.set(0);
        } else if (object instanceof MessageTrailerSegment) {
            MessageTrailerSegment messageTrailer = (MessageTrailerSegment) object;
            messageTrailer.setSegmentCount(SEGMENT_COUNT.get() + 1);
        }

        if (SEGMENT_COUNT.get() == null) {
            SEGMENT_COUNT.set(0);
        }
        SEGMENT_COUNT.set(SEGMENT_COUNT.get() + 1);
    }

    public Object deserialize(SegmentReader segmentReader) {
        String leftString = segmentReader.readUnparsedData();
        int sub = leftString.indexOf(EdifactMessageConstants.AMADEUS_APOSTROPHE);
        if (sub == -1) {
            throw new ParseException("No separator found string= " + leftString);
        }
        // preveriosPosition = currentPosition;
        segmentReader.setReadedCharsCount(sub + 3);
        leftString = leftString.substring(0, sub);
        return deserialize(leftString);
    }

    public Object deserialize(String data) {
       //not accept input
        if (!data.startsWith(getSegmentName())) {
            return null;
        }
        LOG.debug(String.format("Deserialization[class=%s, segmentName=%s] ", cls.getSimpleName(), getSegmentName()));

        Object result;

        try {
            result = cls.newInstance();
        } catch (Exception e) {
            throw new ParseException("Can't create instnace of class = " + cls);
        }

        //parse to strings
        List<List<String>> compositeElements = new ArrayList<List<String>>();
        String[] compositeElementsStrings = data.split(plusSymbol);

        //parse to strings,  we start from 1 becouse we skip segment name
        for (int i = 1; i < compositeElementsStrings.length; ++i) {
            String str = compositeElementsStrings[i];
            compositeElements.add(Arrays.asList(str.split(EdifactMessageConstants.AMADEUS_COLON)));
        }

        //parse to beans
        int i;
        for (i = 0; i < mappings.size(); ++i) {
            Object container;
            if (mappings.get(i).isFlat()) {
                container = result;
            } else {
                try {
                    container = mappings.get(i).getElementClass().newInstance();
                } catch (Exception e) {
                    throw new ParseException("Can't create instance of class = " + mappings.get(i).getElementClass());
                }
                mappings.get(i).setValue(result, container);
            }

            for (int j = 0; j < mappings.get(i).getMapping().size(); ++j) {
                SegmentElementMapping mapping = mappings.get(i).getMapping().get(j);
                String subElementString = getSubElementString(compositeElements, i, j);
                if (subElementString == null) {
                    if (mapping.isRequred()) {
                        throw new ParseException(String.format("No required field[%s] for class[%s] in composite element[%s]", mapping.getPropertyName(),
                                container.getClass(), result.getClass()));
                    }
                } else {
                    Object subElement = deserialize(mapping, subElementString);
                    mapping.setValue(container, subElement);
                }
            }
        }
        i--;

        //parse last composite element in cycle
        if (i >= 0 && mappings.get(i).getCount() > 1) {
            for (int ii = i + 1; ii < compositeElements.size(); ++ii) {
                Object container;
                try {
                    container = mappings.get(i).getElementClass().newInstance();
                } catch (Exception e) {
                    throw new ParseException("Can't create instance of class = " + mappings.get(i).getElementClass());
                }
                mappings.get(i).setValue(result, container);

                for (int j = 0; j < mappings.get(i).getMapping().size(); ++j) {
                    SegmentElementMapping mapping = mappings.get(i).getMapping().get(j);
                    String subElementString = getSubElementString(compositeElements, ii, j);
                    if (subElementString == null) {
                        if (mapping.isRequred()) {
                            throw new ParseException(String.format("No required field[%s] for class[%s] in mapping [%s] in composite element[%s]", mapping
                                    .getPropertyName(), container.getClass(), result.getClass()));
                        }
                    } else {
                        Object subElement = deserialize(mapping, subElementString);
                        mapping.setValue(container, subElement);
                    }
                }
            }
        }


        return result;
    }

    private Object deserialize(SegmentElementMapping mapping, String subElementString) {
        Adapter adapter = mapping.getAdapter();
        if (LOG.isTraceEnabled()) {
            LOG.trace(String.format("Use adapter[%s] for string[%s], field[%s]", adapter.getClass().getSimpleName(), subElementString, mapping.getPropertyName()));
        }
        return adapter.deserialize(subElementString);
    }

    public String getSegmentName() {
        return segmentDeclaration.name();
    }

    public SegmentDeclarationTypeA getSegmentDeclaration() {
        return segmentDeclaration;
    }

    public void setSegmentDeclaration(SegmentDeclarationTypeA segmentDeclaration) {
        this.segmentDeclaration = segmentDeclaration;
    }

    private String getSubElementString(List<List<String>> compositeElements, int i, int j) {
        if (compositeElements.size() - 1 < i || compositeElements.get(i).size() - 1 < j) {
            return null;
        }
        String result = compositeElements.get(i).get(j);
        if (result.length() == 0) {
            return null;
        } else {
            return result;
        }
    }

    public String toString() {
        return String.format("SegmentAdapter[class=%s, code=%s]", cls.getSimpleName(), segmentDeclaration.name());
    }
}
