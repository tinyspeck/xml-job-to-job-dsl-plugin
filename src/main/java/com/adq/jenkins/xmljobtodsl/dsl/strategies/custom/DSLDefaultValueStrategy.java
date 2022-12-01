package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.AbstractDSLStrategy;
import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.List;

public class DSLDefaultValueStrategy extends DSLObjectStrategy {

    private final String name;

    public DSLDefaultValueStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        super(tabs, propertyDescriptor, name, true);
        this.name = name;
    }

    @Override
    public String toDSL() {
        PropertyDescriptor propertyDescriptor = (PropertyDescriptor) getDescriptor();
        List<PropertyDescriptor> children = propertyDescriptor.getProperties();
        for (PropertyDescriptor child : children) {
            if (child.getName().equals("throttleEnabled") && !child.getValue().equals("false")) {
                return replaceTabs(String.format(getSyntax("syntax.object_with_name"), name, getChildrenDSL()), getTabs());
            }
        }
        return "";
    }
}
