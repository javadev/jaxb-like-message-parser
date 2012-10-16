package edi.parser.engine.impl;

import edi.parser.engine.Adapter;
import edi.parser.engine.Group;

import java.lang.reflect.Field;

public class GroupMapping extends Mapping {
    Group groupDate;
    AbstractParser adapter;

    public GroupMapping(Group data, Field propertyName, AbstractParser adapter) {
        super(propertyName);
        this.groupDate = data;
        this.adapter = adapter;
        order = data.order();
    }

    public Adapter getAdapter() {
        return adapter;
    }

    public int getCount() {
        return groupDate.count();
    }

    public boolean isRequred() {
        return groupDate.required();
    }
}
