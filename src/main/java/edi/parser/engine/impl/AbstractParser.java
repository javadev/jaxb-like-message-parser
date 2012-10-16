package edi.parser.engine.impl;

import edi.parser.engine.EnchancedAdapter;

import java.util.List;

public interface AbstractParser extends EnchancedAdapter {
    public void setMappings(List<Mapping> mappings);

    public List<Mapping> getMappings();

    public void setLookahed(int lookahed);
}
