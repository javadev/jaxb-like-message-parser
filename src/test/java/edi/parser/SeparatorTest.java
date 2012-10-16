package edi.parser;

import junit.framework.TestCase;
import edi.parser.engine.Adapter;
import edi.parser.engine.ParseException;
import edi.parser.engine.Segment;
import edi.parser.engine.Separator;
import edi.parser.engine.impl.ParserBuilder;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class SeparatorTest extends TestCase {

    public void test() {
        Bean bean = new Bean();
        bean.setStr1("str1");
        bean.setStr2("str2");
        bean.setStrs(Arrays.asList("str3","str4"));
        Adapter adpter = new ParserBuilder().buildParserTty(Bean.class);

        assertEquals("[before1]str1[after1][before2]str2[after2]str3[between]str4", adpter.serialize(bean));
    }

    public static class Bean {
        @Separator(before = "[before1]", after = "[after1]")
        @Segment(adapter = TestAdapter.class)
        String str1;

        @Separator(before = "[before2]", after = "[after2]")
        @Segment(adapter = TestAdapter.class)
        String str2;

        @Separator(between = "[between]")
        @Segment(adapter = TestAdapter.class, count = 99)
        List<String> strs = new ArrayList();


        public String getStr1() {
            return str1;
        }

        public void setStr1(String str1) {
            this.str1 = str1;
        }

        public String getStr2() {
            return str2;
        }

        public void setStr2(String str2) {
            this.str2 = str2;
        }

        public List<String> getStrs() {
            return strs;
        }

        public void setStrs(List<String> strs) {
            this.strs = strs;
        }
    }

    public static class TestAdapter implements Adapter<String> {
        public String serialize(String object) throws ParseException {
            return object;
        }

        public String deserialize(String data) throws ParseException {
            return data;
        }
    }
}
