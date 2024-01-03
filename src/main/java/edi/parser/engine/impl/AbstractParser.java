package edi.parser.engine.impl;

import edi.parser.engine.EnchancedAdapter;

import java.util.List;

public interface AbstractParser extends EnchancedAdapter {
    void setMappings(List<Mapping> mappings);

    List<Mapping> getMappings();

    void setLookahed(int lookahed);
}
