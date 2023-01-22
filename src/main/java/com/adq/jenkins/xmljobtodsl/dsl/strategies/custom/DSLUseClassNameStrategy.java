package com.adq.jenkins.xmljobtodsl.dsl.strategies.custom;

import com.adq.jenkins.xmljobtodsl.dsl.strategies.DSLObjectStrategy;
import com.adq.jenkins.xmljobtodsl.parsers.PropertyDescriptor;

import java.util.List;

public class DSLUseClassNameStrategy extends DSLObjectStrategy {
    private final String name;

    public DSLUseClassNameStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name) {
        this(tabs, propertyDescriptor, name, true);
        changeChildrensNames(propertyDescriptor);
    }

    public DSLUseClassNameStrategy(int tabs, PropertyDescriptor propertyDescriptor, String name, boolean shouldInitChildren) {
        super(tabs, propertyDescriptor, name, shouldInitChildren);
        this.name = name;
        changeChildrensNames(propertyDescriptor);
    }

    private void changeChildrensNames(PropertyDescriptor propertyDescriptor){
        String className = propertyDescriptor.getAttributes().get("class");
        if(className != null) {
            List<PropertyDescriptor> children = propertyDescriptor.getProperties();
            for(PropertyDescriptor child : children){
                if(child.getAttributes() == null || child.getAttributes().get("ChangedName") != "true") {
                    child.addAttribute("ChangedName", "true");
                    child.changeName(String.format("%s.%s", className, child.getName()));
                }
            }
            initChildren(propertyDescriptor);
        }
    }
}
