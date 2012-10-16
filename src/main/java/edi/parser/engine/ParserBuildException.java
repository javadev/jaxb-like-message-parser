package edi.parser.engine;

public class ParserBuildException extends RuntimeException {
    public ParserBuildException() {
    }

    public ParserBuildException(String message) {
        super(message);
    }

    public ParserBuildException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserBuildException(Throwable cause) {
        super(cause);
    }
}
