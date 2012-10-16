package edi.parser.engine.impl;

import edi.parser.message.EdifactMessageConstants;
import edi.parser.message.common.SegmentCompositeElement;
import edi.parser.message.common.SegmentElement;
import edi.parser.util.ReflectionUtil;
import edi.parser.engine.Adapter;
import edi.parser.engine.BindAnnotation;
import edi.parser.engine.BindClass;
import edi.parser.engine.CustomizableAdapter;
import edi.parser.engine.Group;
import edi.parser.engine.MessageDeclaration;
import edi.parser.engine.ParseException;
import edi.parser.engine.ParserBuildException;
import edi.parser.engine.Segment;
import edi.parser.engine.Separator;
import edi.parser.engine.EnchancedAdapter;
import edi.parser.tty.typeB.common.Parser;
import edi.parser.tty.typeB.common.parser.ParserImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Collections;

/**
 * Class for building MessageParser using mapping. Use top level annotations (@Group, @Segment, @BindAnnotation).
 * All other build code placed in classes tha implements CustomizableAdapter
 * Class not thread safe
 */
public class ParserBuilder {
    private String separator;

    private static Map<Field, Adapter> cache = new WeakHashMap();
    private static Map<Class, Adapter> cache2 = new WeakHashMap();


    public <T> EnchancedAdapter<T> buildParserEdifactA(Class<T> cls) {
        if (cache2.get(cls) != null) {
            return (EnchancedAdapter) cache2.get(cls);
        }
       // separator = EdifactModule.getEdifactSeparator();
        separator = EdifactMessageConstants.AMADEUS_APOSTROPHE;
        MessageParser parser = new MessageParser(cls, separator);

        List<Mapping> mappings = buildMappingList(cls);

        parser.setMappings(mappings);
        parser.setSegmentReaderClass(SegmentReaderImpl.class);
        cache2.put(cls, parser);
        return parser;
    }

    public <T> EnchancedAdapter<T> buildParserTty(Class<T> cls) {
        if (cache2.get(cls) != null) {
            return (EnchancedAdapter) cache2.get(cls);
        }
        separator = "\r\n";
        MessageParser parser = new MessageParser(cls, separator);
        List<Mapping> mappings = buildMappingList(cls);

        parser.setMappings(mappings);
        parser.setSegmentReaderClass(SegmentReaderOneStringImpl.class);
        cache2.put(cls, parser);
        return parser;
    }

    public <T> EnchancedAdapter<T> buildParserTty(Class<T> cls, int lookahead) {
        if (cache2.get(cls) != null) {
            return (EnchancedAdapter) cache2.get(cls);
        }
        separator = "\r\n";
        MessageParser parser = new MessageParser(cls, separator);
        parser.lookahed = lookahead;
        parser.useLookahedWithAllRequired = false;
        List<Mapping> mappings = buildMappingList(cls);

        parser.setMappings(mappings);
        parser.setSegmentReaderClass(SegmentReaderOneStringImpl.class);
        cache2.put(cls, parser);
        return parser;
    }

    /**
     * Parser build method for BSP hot files
     * @param <T>
     * @param cls
     * @return
     */
    public <T> EnchancedAdapter<T> buildParserBSP(Class<T> cls) {
        if (cache2.get(cls) != null) {
            return (EnchancedAdapter) cache2.get(cls);
        }
        separator = "\r\n";
        MessageParser parser = new MessageParser(cls, separator);

        List<Mapping> mappings = buildMappingList(cls);

        parser.setMappings(mappings);
        parser.setSegmentReaderClass(SegmentReaderImpl.class);
        cache2.put(cls, parser);
        return parser;
    }

    public Parser buildTypeBParser(Class[] classes) {
        ParserImpl parser = new ParserImpl();
        for (Class messageClass : classes) {
            Adapter messageParser = buildParserTty(messageClass);
            MessageDeclaration decl = (MessageDeclaration) messageClass.getAnnotation(MessageDeclaration.class);
            if (decl == null) {
                throw new ParserBuildException("No @MessageDeclaration on class=" + messageClass);
            }
            parser.getAdapters().put(decl.name(), messageParser);
        }
        return parser;
    }

    private List<Mapping> buildMappingList(Class cls) {
        List<Mapping> mappings = new ArrayList<Mapping>();
        List<Field> fields = ReflectionUtil.getAllFields(cls);

        for (Field field : fields) {
            if (!hasAnnotations(field)) {
                continue;
            }

            Mapping mapping = null;
            if (getBindAnnotation(field) != null) {
                Segment segment = field.getAnnotation(Segment.class);
                if (segment == null) {
                    segment = BindAnnotation.class.getAnnotation(Segment.class);
                }
                mapping = new SegmentMapping(segment, field, createSegmentAdapter(segment.adapter(), ReflectionUtil.typeExtractor(field), field));
            } else if (field.getAnnotation(Segment.class) != null) {
                Segment segment = field.getAnnotation(Segment.class);
                mapping = new SegmentMapping(segment, field, createSegmentAdapter(segment.adapter(), ReflectionUtil.typeExtractor(field), field));
            } else if (field.getAnnotation(Group.class) != null) {
                Group group = field.getAnnotation(Group.class);
                mapping = createGroupMapping(field);
            } else {
                throw new ParserBuildException(String.format("MessageParser build: Filed[%s] of class[%s] not annoted with @Segement or @Group", field.getName(), field
                        .getDeclaringClass().getSimpleName()));
            }

            // add separator to mapping
            Separator separator = field.getAnnotation(Separator.class);
            if (separator != null) {
                SeparatorImpl separatorImpl = new SeparatorImpl(separator);
                mapping.setSeparator(separatorImpl);
            }

            mappings.add(mapping);
        }

        //sort mappings
        Collections.sort(mappings);
        return mappings;
    }

    private boolean hasAnnotations(Field field) {
        return field.getAnnotation(Group.class) != null || field.getAnnotation(Segment.class) != null
                || field.getAnnotation(SegmentElement.class) != null || field.getAnnotation(SegmentCompositeElement.class) != null
                || getBindAnnotation(field) != null;
    }

    private BindAnnotation getBindAnnotation(Field field) {
        BindAnnotation bindAnnotation = null;
        for (Annotation a : field.getAnnotations()) {
            bindAnnotation = a.annotationType().getAnnotation(BindAnnotation.class);
            if (bindAnnotation != null) {
                break;
            }
        }
        return bindAnnotation;
    }



    protected Mapping createGroupMapping(Field mappedField) {
        Class cls = ReflectionUtil.typeExtractor(mappedField);
        Group group = mappedField.getAnnotation(Group.class);
        AbstractParser parser = (AbstractParser) cache.get(mappedField);
        if (parser == null) {
            parser = createParserForGroup(cls);
            List<Mapping> mappings = buildMappingList(cls);
            parser.setMappings(mappings);
            parser.setLookahed(group.lookahead());
            cache.put(mappedField, parser);
        }
        GroupMapping groupMapping = new GroupMapping(group, mappedField, parser);
        return groupMapping;
    }

    protected AbstractParser createParserForGroup(Class cls) {
        return new GroupParser(cls, separator);
    }

    /**
     * build segement adapter
     * Order of adapter class :
     * 1. Try get from class using @BindClass
     * 2. Try get from spec annotation using @BindAnnotation
     * 3. Use defined in segment
     * @param segment
     * @param type
     * @return
     */
    protected Adapter createSegmentAdapter(Class< ? extends Adapter> adapterClass, Class< ? > type, Field fieldAdapter) {

        Adapter adapter = cache.get(fieldAdapter);
        if (adapter != null) {
            return adapter;
        }

        try {
            BindClass bindClassAnnotation = type.getAnnotation(BindClass.class);
            if (bindClassAnnotation != null) {
                adapterClass = bindClassAnnotation.adapter();
                // TODO: init data
            }

            // create using BindAnnotation not segment
            BindAnnotation bindAnnotation = getBindAnnotation(fieldAdapter);
            if (bindAnnotation != null) {
                adapterClass = bindAnnotation.adapter();
            }

            adapter = adapterClass.newInstance();

            if (adapter instanceof CustomizableAdapter) {
                ((CustomizableAdapter) adapter).setField(fieldAdapter);
            }
            cache.put(fieldAdapter, adapter);
            return adapter;
        } catch (Exception e) {
            throw new ParseException(String.format("Can't build segment adpater[type=%s]", type), e);
        }
    }

}
