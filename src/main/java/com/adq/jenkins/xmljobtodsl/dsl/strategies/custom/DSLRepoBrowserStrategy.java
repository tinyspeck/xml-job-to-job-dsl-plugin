package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DSLRepoBrowserStrategy extends DSLObjectStrategy {

    private final String name;

    public DSLRepoBrowserStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        this(tabs, propertyDescriptor, name, true);
    }

    public DSLRepoBrowserStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, name, shouldInitChildren);
        this.name = name;

        List<PropertyDescriptor> newChildren = new ArrayList<>();

        // Extract the browser name
        String attribute = propertyDescriptor.getAttributes().toString();
        Pattern pattern = Pattern.compile("hudson\\.plugins\\.git\\.browser\\.(\\w+)");
        Matcher matcher = pattern.matcher(attribute);
        String extractedString = null;
        String lowercaseString = null;
        if (matcher.find()) {
            extractedString = matcher.group(1);
            lowercaseString = extractedString.replaceFirst("^.", extractedString.substring(0, 1).toLowerCase());
        }

        PropertyDescriptor newChild = new PropertyDescriptor(
                String.format(lowercaseString),
                propertyDescriptor,
                propertyDescriptor.getProperties());

        newChildren.add(newChild);
        propertyDescriptor.replaceProperties(newChildren);

        initChildren(propertyDescriptor);

    }

    public String toDSL() {
        String childrenDSL = getChildrenDSL();
        if (childrenDSL.isEmpty()) {
            return "";
        }

        if (name != null) {
            return replaceTabs(String.format(getSyntax("syntax.object_with_name"), name, childrenDSL), getTabs());
        } else {
            return replaceTabs(String.format(getSyntax("syntax.object"), childrenDSL), getTabs());
        }
    }
}