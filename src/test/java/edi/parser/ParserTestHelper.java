package edi.parser;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

public class ParserTestHelper {

    static List<String> loggers = Arrays.asList(
            "net.worldticket.edi.protocol",
            "net.worldticket.edi.protocol.edifact.message",
            "net.worldticket.edi.parser"
    );
    static Level toLevel = Level.INFO;
    static Map<String, Level> logToLevel = new HashMap<>();

    public static void turnOffParserLog() {
        for(String log : loggers) {
            logToLevel.put(log, Logger.getLogger(log).getLevel());
            Logger.getLogger(log).setLevel(toLevel);
        }
    }

    public static void turnOnParserLog() {
        for (String log : loggers) {
            Logger.getLogger(log).setLevel(logToLevel.get(log));
        }
    }
}
