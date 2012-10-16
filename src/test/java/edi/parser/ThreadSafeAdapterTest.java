package edi.parser;

import junit.framework.TestCase;
import edi.parser.engine.Adapter;
import edi.parser.engine.ParseException;
import edi.parser.engine.ThreadSafeAdapter;
import edi.parser.engine.Segment;
import edi.parser.engine.impl.ParserBuilder;

/**
 * Verify that parser engine create adapter for each serelization call.
 */
public class ThreadSafeAdapterTest extends TestCase {
    public void test0 () {
        String data="qwer\r\nasdasd" +
                "\r\nLine3\r\nLine4<>\r\n!@#_Line5__))(*\r\n\r\n\r\nLine8\n" +
                "Check Unix \\n";
        Adapter parser = new ParserBuilder().buildParserEdifactA(Bean.class);

        assertEquals(data, parser.serialize(parser.deserialize(data)));
        assertEquals(data, parser.serialize(parser.deserialize(data)));
        assertEquals(data, parser.serialize(parser.deserialize(data)));
        assertEquals((4)*3 +2,Adapter1.createdTimes);
        assertEquals(1, Adapter2.createdTimes);
    }

    public static class Bean {
        @Segment(adapter = Adapter1.class)
        String str1;
        @Segment(adapter = Adapter2.class)
        String str2;
        @Segment(adapter = Adapter1.class)
        //List<String> strings = new ArrayList();
        String str3;
    }

   public static class Adapter1 implements Adapter<String> {
       public static int createdTimes;

       public Adapter1() {
        createdTimes++;
       }

       public String serialize(String object) throws ParseException {
           return object;
       }

       public String deserialize(String data) throws ParseException {
           return data;
       }
   }

   public static class Adapter2 implements Adapter<String>, ThreadSafeAdapter {
       public static int createdTimes;

       public Adapter2() {
        createdTimes++;
       }

       public String serialize(String object) throws ParseException {
           return object;
       }

       public String deserialize(String data) throws ParseException {
           return data;
       }
   }
}
