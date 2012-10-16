package edi.parser.engine.impl;

import edi.parser.engine.Separator;

public class SeparatorImpl {
    private String before = "";
    private String after = "";
    private String between = "";

    public SeparatorImpl() {
    }

    public SeparatorImpl(Separator data) {
        before = data.before();
        after = data.after();
        between = data.between();
    }

    public String getBefore() {
        return before;
    }

    public String getAfter() {
        return after;
    }


    public String getBetween() {
        return between;
    }
}
