package edi.parser.common.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edi.parser.common.CodedEnum;
import edi.parser.common.EnumAdapter;
import edi.parser.engine.CustomizableAdapter;
import edi.parser.engine.EnchancedAdapter;
import edi.parser.engine.ParseException;
import edi.parser.engine.ParserBuildException;
import edi.parser.engine.SegmentReader;
import edi.parser.util.CollectionUtil;
import edi.parser.util.Predicate;

import org.apache.log4j.Logger;

public class EnumAdapterImpl implements EnchancedAdapter, CustomizableAdapter {
    private static final Logger LOG = Logger.getLogger(EnumAdapterImpl.class);

    private Pattern regExp;

    private Class enumClass;

    private Method valuesMethod;

    public Object deserialize(String data) throws ParseException {
        return null;
    }

    public String serialize(Object object) throws ParseException {
        // TODO Auto-generated method stub
        return null;
    }

    public Object deserialize(SegmentReader segmentReader) throws ParseException {
        String data = segmentReader.readUnparsedData();
        Matcher m = regExp.matcher(data);
        if (m.find() && m.start() == 0) {
            String code = m.group();
            Object res = findByCode(code);
            if (res != null) {
                segmentReader.setReadedCharsCount(code.length());
                LOG.debug("Parsed: " + code);
                return res;
            }
        }
        return null;
    }

    private CodedEnum findByCode(final String code) {
        CodedEnum[] values;
        try {
            values = (CodedEnum[]) valuesMethod.invoke(null);
        } catch (Exception e) {
            throw new ParseException("Can't invoke method " + valuesMethod, e);
        }
        return CollectionUtil.findFirst(Arrays.asList(values), new Predicate<CodedEnum>() {
            public boolean evaluate(CodedEnum object) {
                return code.equals(object.getCode());
            }

        });

    }

    public void setField(Field field) throws Exception {
        EnumAdapter annot = field.getAnnotation(EnumAdapter.class);
        if (annot == null) {
            throw new ParserBuildException("No annotation @EnumAdapter on field " + field);
        }
        enumClass = field.getType();
        regExp = Pattern.compile(annot.regexp());
        valuesMethod = enumClass.getMethod("values", new Class[] {});
        if (!enumClass.isEnum() || !CodedEnum.class.isAssignableFrom(enumClass)) {
            throw new ParserBuildException("Incorrect enum class[not impleemtn CodedEnum or is not enum], class = " + enumClass);
        }
    }

}
