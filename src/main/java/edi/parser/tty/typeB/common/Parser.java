package edi.parser.tty.typeB.common;

public interface Parser {
    public Object deserialize(String data);

    public String getMessageIdentifier(String data);
}
