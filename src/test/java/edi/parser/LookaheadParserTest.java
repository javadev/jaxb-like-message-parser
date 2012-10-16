package edi.parser;

import junit.framework.TestCase;
import edi.parser.engine.Adapter;
import edi.parser.engine.ParseException;
import edi.parser.engine.EnchancedAdapter;
import edi.parser.engine.SegmentReader;
import edi.parser.engine.Group;
import edi.parser.engine.Segment;
import edi.parser.engine.impl.ParserBuilder;
import edi.parser.message.adapter.IntegerAdapter;

public class LookaheadParserTest extends TestCase {
    public void test0() {
        String str = "A123B";
        Adapter<Message> adapter = new ParserBuilder().buildParserTty(Message.class);
        Message msg = adapter.deserialize(str);
        assertEquals("A", msg.getBean().getStr());
        assertEquals(123, msg.getBean().getI());
        assertEquals("B", msg.getStr());
    }

    /**
     * Message
     */
    public static class Message {
        @Group(lookahead = 2)
        private Bean bean;
        @Segment(adapter = CharAdapter.class)
        private String str;

        public Bean getBean() {
            return bean;
        }

        public void setBean(Bean bean) {
            this.bean = bean;
        }

        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }
    }

    /**
     * Bean
     */
    public static class Bean {
        @Segment(adapter = CharAdapter.class)
        private String str;
        @Segment(adapter = IntegerAdapter.class)
        private int i;


        public String getStr() {
            return str;
        }

        public void setStr(String str) {
            this.str = str;
        }

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }
    }

    /**
     * CharAdapter
     */
    public static class CharAdapter implements EnchancedAdapter<String>   {


        public String deserialize(SegmentReader segmentReader) {
            String object = segmentReader.readSegment();
            if (object.substring(0, 1).matches("[^0-9]")) {
                segmentReader.setReadedCharsCount(1);
                return object.substring(0, 1);
            } else {
                return null;
            }
        }

        public String serialize(String object) throws ParseException {
            if (object.substring(0, 1).matches("^[0-9]")) {
                return object.substring(0, 1);
            } else {
                return null;
            }
        }

        public String deserialize(String data) throws ParseException {
            throw new UnsupportedOperationException();
        }
    }


}
