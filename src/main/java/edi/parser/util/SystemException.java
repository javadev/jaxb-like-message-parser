package edi.parser.util;

public class SystemException extends RuntimeException {

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable trowable) {
        super(message, trowable);
    }

    public SystemException(Throwable trowable) {
        super(trowable);
    }

}

